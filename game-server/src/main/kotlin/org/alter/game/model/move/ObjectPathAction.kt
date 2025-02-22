package org.alter.game.model.move

import dev.openrune.cache.CacheManager.getObject
import gg.rsmod.util.AabbUtil
import gg.rsmod.util.DataConstants
import net.rsprot.protocol.game.outgoing.misc.player.SetMapFlag
import org.alter.game.model.Direction
import org.alter.game.model.Tile
import org.alter.game.model.attr.INTERACTING_ITEM
import org.alter.game.model.attr.INTERACTING_OBJ_ATTR
import org.alter.game.model.attr.INTERACTING_OPT_ATTR
import org.alter.game.model.entity.Entity
import org.alter.game.model.entity.GameObject
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.game.model.queue.TaskPriority
import org.alter.game.model.timer.FROZEN_TIMER
import org.alter.game.model.timer.STUN_TIMER
import org.alter.game.plugin.Plugin
import org.rsmod.routefinder.Route
import org.rsmod.routefinder.collision.CollisionStrategy
import org.rsmod.routefinder.loc.LocShapeConstants
import java.util.*

/**
 * This class is responsible for calculating distances and valid interaction
 * tiles for [GameObject] route-finding.
 *
 * @author Tom <rspsmods@gmail.com>
 */
object ObjectPathAction {
    fun walk(
        player: Player,
        obj: GameObject,
        lineOfSightRange: Int?,
        logic: Plugin.() -> Unit,
    ) {
        player.queue(TaskPriority.STANDARD) {
            terminateAction = {
                player.stopMovement()
                player.write(SetMapFlag(255, 255))
            }

            val route = walkTo(player, obj, lineOfSightRange)
            if (route.success) {
                if (lineOfSightRange == null || lineOfSightRange > 0) {
                    faceObj(player, obj)
                }
                player.executePlugin(logic)
            } else {
                player.faceTile(obj.tile)
                when {
                    player.timers.has(FROZEN_TIMER) -> player.writeMessage(Entity.MAGIC_STOPS_YOU_FROM_MOVING)
                    player.timers.has(STUN_TIMER) -> player.writeMessage(Entity.YOURE_STUNNED)
                    else -> player.writeMessage(Entity.YOU_CANT_REACH_THAT)
                }
                player.write(SetMapFlag(255, 255))
            }
        }
    }

    val itemOnObjectPlugin: Plugin.() -> Unit = {
        val player = ctx as Player

        val item = player.attr[INTERACTING_ITEM]!!.get()!!
        val obj = player.attr[INTERACTING_OBJ_ATTR]!!.get()!!
        val lineOfSightRange = player.world.plugins.getObjInteractionDistance(obj.id)

        walk(player, obj, lineOfSightRange) {
            if (!player.world.plugins.executeItemOnObject(player, obj.getTransform(player), item.id)) {
                player.writeMessage(Entity.NOTHING_INTERESTING_HAPPENS)
                if (player.world.devContext.debugObjects) {
                    player.writeMessage(
                        "Unhandled item on object: [item=$item, id=${obj.id}, type=${obj.type}, rot=${obj.rot}, x=${obj.tile.x}, y=${obj.tile.z}]",
                    )
                }
            }
        }
    }

    val objectInteractPlugin: Plugin.() -> Unit = {
        val player = ctx as Player

        val obj = player.attr[INTERACTING_OBJ_ATTR]!!.get()!!
        val opt = player.attr[INTERACTING_OPT_ATTR]
        val lineOfSightRange = player.world.plugins.getObjInteractionDistance(obj.id)

        walk(player, obj, lineOfSightRange) {
            if (!player.world.plugins.executeObject(player, obj.getTransform(player), opt!!)) {
                player.writeMessage(Entity.NOTHING_INTERESTING_HAPPENS)
                if (player.world.devContext.debugObjects) {
                    player.writeMessage(
                        "Unhandled object action: [opt=$opt, id=${obj.id}, type=${obj.type}, rot=${obj.rot}, x=${obj.tile.x}, y=${obj.tile.z}]",
                    )
                }
            }
        }
    }

    private suspend fun QueueTask.walkTo(
        pawn: Pawn,
        obj: GameObject,
        lineOfSightRange: Int?,
    ): Route {
        val def = obj.getDef()
        val tile = obj.tile
        val type = obj.type
        val rot = obj.rot
        var width = def.sizeX
        var length = def.sizeY
        val clipMask = def.clipMask

        val wall = type == LocShapeConstants.WALL_STRAIGHT || type == LocShapeConstants.WALL_DIAGONAL
        val diagonal = type == LocShapeConstants.WALL_DIAGONAL || type == LocShapeConstants.CENTREPIECE_DIAGONAL
        val wallDeco = type == LocShapeConstants.WALLDECOR_STRAIGHT_NOOFFSET || type == LocShapeConstants.WALLDECOR_STRAIGHT_OFFSET
        val blockDirections = EnumSet.noneOf(Direction::class.java)

        if (wallDeco) {
            width = 0
            length = 0
        } else if (!wall && (rot == 1 || rot == 3)) {
            width = def.sizeY
            length = def.sizeX
        }

        /*
         * Objects have a clip mask in their [ObjectDef] which can be used
         * to specify any directions that the object can't be 'interacted'
         * from.
         */
        val blockBits = 4
        val clipFlag = (DataConstants.BIT_MASK[blockBits] and (clipMask shl rot)) or (clipMask shr (blockBits - rot))

        if ((0x1 and clipFlag) != 0) {
            blockDirections.add(Direction.NORTH)
        }

        if ((0x2 and clipFlag) != 0) {
            blockDirections.add(Direction.EAST)
        }

        if ((0x4 and clipFlag) != 0) {
            blockDirections.add(Direction.SOUTH)
        }

        if ((clipFlag and 0x8) != 0) {
            blockDirections.add(Direction.WEST)
        }

        /*
         * Wall objects can't be interacted from certain directions due to
         * how they are visually placed in a tile.
         */
        val blockedWallDirections =
            when (rot) {
                0 -> EnumSet.of(Direction.EAST)
                1 -> EnumSet.of(Direction.SOUTH)
                2 -> EnumSet.of(Direction.WEST)
                3 -> EnumSet.of(Direction.NORTH)
                else -> throw IllegalStateException("Invalid object rotation: $rot")
            }

        /*
         * Diagonal walls have an extra direction set as 'blocked', this is to
         * avoid the player interacting with the door and having its opened
         * door object be spawned on top of them, which leads to them being
         * stuck.
         */
        if (wall && diagonal) {
            when (rot) {
                0 -> blockedWallDirections.add(Direction.NORTH)
                1 -> blockedWallDirections.add(Direction.EAST)
                2 -> blockedWallDirections.add(Direction.SOUTH)
                3 -> blockedWallDirections.add(Direction.WEST)
            }
        }

        if (wall) {
            /*
             * Check if the pawn is within interaction distance of the wall.
             */
            if (pawn.tile.isWithinRadius(tile, 1)) {
                val dir = Direction.between(tile, pawn.tile)
                if (dir !in blockedWallDirections &&
                    (
                            diagonal ||
                                    !AabbUtil.areDiagonal(
                                        pawn.tile.x,
                                        pawn.tile.z,
                                        pawn.getSize(),
                                        pawn.getSize(),
                                        tile.x,
                                        tile.z,
                                        width,
                                        length,
                                    )
                            )
                ) {
                    return Route(Pawn.EMPTY_TILE_DEQUE, alternative = false, success = true)
                }
            }

            blockDirections.addAll(blockedWallDirections)
        }

        val route = pawn.world.smartRouteFinder.findRoute(
                level = pawn.tile.height,
                srcX = pawn.tile.x,
                srcZ = pawn.tile.z,
                destX = tile.x,
                destZ = tile.z,
                destWidth = def.sizeX,
                destLength = def.sizeY,
                srcSize = pawn.getSize(), // @TODO ?
                collision = CollisionStrategy.Normal,
                locAngle = obj.rot,
                locShape = obj.type,
                blockAccessFlags = def.clipMask,
            )
        pawn.walkRoute(route, MovementQueue.StepType.NORMAL)
        val last = pawn.movementQueue.peekLast()

        while (last != null &&
            !pawn.tile.sameAs(last) &&
            !pawn.timers.has(FROZEN_TIMER) &&
            !pawn.timers.has(STUN_TIMER) &&
            pawn.lock.canMove()
        ) {
            wait(1)
        }

        if (pawn.timers.has(STUN_TIMER)) {
            pawn.stopMovement()
            return Route.FAILED
        }

        if (pawn.timers.has(FROZEN_TIMER)) {
            pawn.stopMovement()
            return Route.FAILED
        }

        if (wall && !route.success && Direction.between(tile, pawn.tile) !in blockedWallDirections) {
            // Here we assume that route.waypoints is already of type List<RouteCoordinates>
            return Route(route.waypoints, alternative = false, success = true)
        }
        // Find the nearest tile within the object's dimensions to the player
        val nearestTile = findNearestTile(pawn.tile, tile, width, length, rot)

        /**
         * @TODO Inspect
         */
        //if (def.name.contains("Furnace")) {
        //    tile =
        //        when (rot) {
        //            0 -> tile.transform(0, width shr 1)
        //            1 -> tile.transform(width shr 1, 0)
        //            else -> tile
        //        }
        //}

        //val isPathBlocked =
        //    if (def.name.contains("Furnace")) {
        //        pawn.isPathBlocked(tile)
        //    } else {
        //        pawn.isPathBlocked(nearestTile)
        //    }

        val radius = lineOfSightRange ?: 1

        val isWithinRadius = pawn.tile.isWithinRadius(nearestTile, radius)
        // Ensure the route is successful only if the player is within interaction range to the nearest object tile
        if (route.success && (isWithinRadius) /** && (!isPathBlocked || wall || wallDeco)**/ ) {
            // println("isBlocked: $isPathBlocked, nearestTile: $nearestTile, isWithinRadius: $isWithinRadius, radius: $radius")
            return route
        }
        // println("isBlocked: $isPathBlocked, nearestTile: $nearestTile, isWithinRadius: $isWithinRadius, radius: $radius")
        return Route.FAILED
    }
    private fun findNearestTile(
        playerTile: Tile,
        objectTile: Tile,
        width: Int,
        length: Int,
        rotation: Int,
    ): Tile {
        val adjustedWidth = if (rotation == 1 || rotation == 3) length else width
        val adjustedLength = if (rotation == 1 || rotation == 3) width else length

        val nearestX = playerTile.x.coerceIn(objectTile.x..objectTile.x + adjustedWidth)
        val nearestZ = playerTile.z.coerceIn(objectTile.z..objectTile.z + adjustedLength)

        return Tile(nearestX, nearestZ, objectTile.height)
    }
    private fun faceObj(
        pawn: Pawn,
        obj: GameObject,
    ) {
        val def = getObject(obj.id)
        val rot = obj.rot
        val type = obj.type

        when (type) {
            LocShapeConstants.WALL_STRAIGHT -> {
                if (!pawn.tile.sameAs(obj.tile)) {
                    pawn.faceTile(obj.tile)
                }
            }
            LocShapeConstants.WALLDECOR_STRAIGHT_NOOFFSET, LocShapeConstants.WALLDECOR_STRAIGHT_OFFSET -> {
                val dir =
                    when (rot) {
                        0 -> Direction.WEST
                        1 -> Direction.NORTH
                        2 -> Direction.EAST
                        3 -> Direction.SOUTH
                        else -> throw IllegalStateException("Invalid object rotation: $obj")
                    }
                pawn.faceTile(pawn.tile.step(dir))
            }
            else -> {
                var width = def.sizeX
                var length = def.sizeY
                if (rot == 1 || rot == 3) {
                    width = def.sizeY
                    length = def.sizeX
                }
                pawn.faceTile(obj.tile.transform(width shr 1, length shr 1), width, length)
            }
        }
    }
}
