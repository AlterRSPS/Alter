package org.alter.game.model.move

import gg.rsmod.util.AabbUtil
import net.rsprot.protocol.game.outgoing.misc.player.SetMapFlag
import org.alter.game.model.Tile
import org.alter.game.model.attr.*
import org.alter.game.model.collision.raycast
import org.alter.game.model.entity.Entity
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.game.model.queue.TaskPriority
import org.alter.game.model.timer.FROZEN_TIMER
import org.alter.game.model.timer.RESET_PAWN_FACING_TIMER
import org.alter.game.model.timer.STUN_TIMER
import org.alter.game.plugin.Plugin
import org.rsmod.game.pathfinder.Route
import org.rsmod.game.pathfinder.collision.CollisionStrategies
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
object PawnPathAction {
    private const val ITEM_USE_OPCODE = -1

    val walkPlugin: Plugin.() -> Unit = {
        val pawn = ctx as Pawn
        val world = pawn.world
        val other = pawn.attr[INTERACTING_NPC_ATTR]?.get() ?: pawn.attr[INTERACTING_PLAYER_ATTR]?.get()!!
        val opt = pawn.attr[INTERACTING_OPT_ATTR]!!

        /*
         * Some interactions only require line-of-sight range, such as npcs
         * behind cells or booths. This allows for diagonal interaction.
         *
         * Set to null for default interaction range.
         */
        val lineOfSightRange = if (other is Npc) world.plugins.getNpcInteractionDistance(other.id) else null

        pawn.queue(TaskPriority.STANDARD) {
            terminateAction = {
                pawn.stopMovement()
                if (pawn is Player) {
                    pawn.write(SetMapFlag(255, 255))
                }
            }

            walk(this, pawn, other, opt, lineOfSightRange)
        }
    }

    val itemUsePlugin: Plugin.() -> Unit = s@{
        val pawn = ctx as Pawn
        val world = pawn.world
        val other = pawn.attr[INTERACTING_NPC_ATTR]?.get() ?: pawn.attr[INTERACTING_PLAYER_ATTR]?.get()!!

        /*
         * Some interactions only require line-of-sight range, such as npcs
         * behind cells or booths. This allows for diagonal interaction.
         *
         * Set to null for default interaction range.
         */
        val lineOfSightRange = if (other is Npc) world.plugins.getNpcInteractionDistance(other.id) else null

        pawn.queue(TaskPriority.STANDARD) {
            terminateAction = {
                pawn.stopMovement()
                if (pawn is Player) {
                    pawn.write(SetMapFlag(255, 255))
                }
            }

            walk(this, pawn, other, ITEM_USE_OPCODE, lineOfSightRange)
        }
    }

    private suspend fun walk(
        it: QueueTask,
        pawn: Pawn,
        other: Pawn,
        opt: Int,
        lineOfSightRange: Int?,
    ) {
        val world = pawn.world
        val initialTile = Tile(other.tile)

        pawn.facePawn(other)


        val pathFound =
            walkTo(
                it,
                pawn,
                other,
                lineOfSightRange = lineOfSightRange ?: 1,
                lineOfSight =
                lineOfSightRange != null,
            )
        if (!pathFound) {
            pawn.movementQueue.clear()
            if (pawn is Player) {
                when {
                    pawn.timers.has(FROZEN_TIMER) -> pawn.writeMessage(Entity.MAGIC_STOPS_YOU_FROM_MOVING)
                    pawn.timers.has(STUN_TIMER) -> pawn.writeMessage(Entity.YOURE_STUNNED)
                    else -> pawn.writeMessage(Entity.YOU_CANT_REACH_THAT)
                }
                pawn.write(SetMapFlag(255, 255))
            }
            pawn.resetFacePawn()
            return
        }

        pawn.stopMovement()

        if (pawn is Player) {
            if (pawn.attr[FACING_PAWN_ATTR]?.get() != other) {
                return
            }
            /*
             * If the npc has moved from the time this queue was added to
             * when it was actually invoked, we need to walk towards it again.
             */
            if (!other.tile.sameAs(initialTile)) {
                walk(it, pawn, other, opt, lineOfSightRange)
                return
            }

            if (other is Npc) {
                /*
                 * On 07, only one npc can be facing the player at a time,
                 * so if the last pawn that faced the player is still facing
                 * them, then we reset their face target.
                 */
                pawn.attr[NPC_FACING_US_ATTR]?.get()?.let {
                    if (it.attr[FACING_PAWN_ATTR]?.get() == pawn) {
                        it.resetFacePawn()
                        it.timers.remove(RESET_PAWN_FACING_TIMER)
                    }
                }
                pawn.attr[NPC_FACING_US_ATTR] = WeakReference(other)

                /*
                 * Stop the npc from walking while the player talks to it
                 * for [Npc.RESET_PAWN_FACE_DELAY] cycles.
                 * @TODO review
                 */
                //other.stopMovement()
                //if (other.attr[FACING_PAWN_ATTR]?.get() != pawn && !other.static) {
                //    other.facePawn(pawn)
                //    other.timers[RESET_PAWN_FACING_TIMER] = Npc.RESET_PAWN_FACE_DELAY
                //}

                val npcId = other.getTransform(pawn)
                val handled =
                    if (opt != ITEM_USE_OPCODE) {
                        world.plugins.executeNpc(pawn, npcId, opt)
                    } else {
                        val item = pawn.attr[INTERACTING_ITEM]?.get() ?: return
                        world.plugins.executeItemOnNpc(pawn, npcId, item.id)
                    }

                if (!handled) {
                    pawn.writeMessage(Entity.NOTHING_INTERESTING_HAPPENS)
                }
            }

            if (other is Player) {
                if (opt != ITEM_USE_OPCODE) {
                    val option = pawn.options[opt - 1]
                    if (option != null) {
                        val handled = world.plugins.executePlayerOption(pawn, option)
                        if (!handled) {
                            pawn.writeMessage(Entity.NOTHING_INTERESTING_HAPPENS)
                        }
                    }
                }
            }
            pawn.resetFacePawn()
            pawn.faceTile(other.tile)
        }
    }

    suspend fun walkTo(
        it: QueueTask,
        pawn: Pawn,
        target: Pawn,
        lineOfSightRange: Int,
        lineOfSight: Boolean,
    ): Boolean {
        val sourceSize = pawn.getSize()
        val targetSize = target.getSize()
        val sourceTile = pawn.tile
        val targetTile = target.tile
        val projectile = lineOfSightRange > 2 /** @TODO What kind of retard wrote this??? **/
        println("lineOfSightRange: $lineOfSightRange")
        val frozen = pawn.timers.has(FROZEN_TIMER)
        val stunned = pawn.timers.has(STUN_TIMER)

        if (pawn.attr[FACING_PAWN_ATTR]?.get() != target) {
            return false
        }

        if (stunned) {
            return false
        }

        if (frozen) {
            if (overlap(sourceTile, sourceSize, targetTile, targetSize)) {
                return false
            }

            if (!projectile) {
                return if (!lineOfSight) {
                    bordering(sourceTile, sourceSize, targetTile, lineOfSightRange)
                } else {
                    overlap(sourceTile, sourceSize, targetTile, lineOfSightRange) &&
                            (lineOfSightRange == 0 || !sourceTile.sameAs(targetTile)) &&
                            pawn.world.collision.raycast(sourceTile, targetTile, lineOfSight)
                }
            }
        }

        val newRoute =
            pawn.world.pathFinder.findPath(
                level = pawn.tile.height,
                srcX = sourceTile.x,
                srcZ = sourceTile.z,
                destX = targetTile.x,
                destZ = targetTile.z,
                srcSize = sourceSize,
                destWidth = targetSize,
                destHeight = targetSize,
                collision = CollisionStrategies.Normal,
            )
        val tileQueue: Queue<Tile> = ArrayDeque(newRoute.waypoints.map { Tile(it.x, it.z, it.level) })

        pawn.walkPath(tileQueue, MovementQueue.StepType.NORMAL, detectCollision = false)
        if (pawn.hasLineOfSightTo(target, true, lineOfSightRange) && projectile) {
            return newRoute.success
        }
        while (!pawn.tile.isWithinRadius(targetTile, lineOfSightRange)) {
            if (!targetTile.sameAs(target.tile)) {
                return walkTo(it, pawn, target, lineOfSightRange, lineOfSight)
            }
            it.wait(1)
        }
        return newRoute.success
    }

    private fun overlap(
        tile1: Tile,
        size1: Int,
        tile2: Tile,
        size2: Int,
    ): Boolean = AabbUtil.areOverlapping(tile1.x, tile1.z, size1, size1, tile2.x, tile2.z, size2, size2)

    private fun bordering(
        tile1: Tile,
        size1: Int,
        tile2: Tile,
        size2: Int,
    ): Boolean = AabbUtil.areBordering(tile1.x, tile1.z, size1, size1, tile2.x, tile2.z, size2, size2)




    fun Pawn.combatPath(target: Pawn) {
        val route = world.pathFinder.findPath(
            level = this.tile.height,
            srcX = this.tile.x,
            srcZ = this.tile.z,
            destX = target.tile.x,
            destZ = target.tile.z,
            moveNear = true,
            objShape = -2
        )
        if (route == Route.FAILED) {
            return
        }
        val tileQueue: Queue<Tile> = ArrayDeque(route.waypoints.map { Tile(it.x, it.z, it.level) })
        this.walkPath(tileQueue, MovementQueue.StepType.NORMAL, detectCollision = true)
        /**
         * @TODO
         */
        if (this.tile.isWithinRadius(target.tile, target.getSize())) {
            attack(target)
        }
    }
}