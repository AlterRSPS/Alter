package org.alter.game.model.move

import net.rsprot.protocol.game.outgoing.misc.player.SetMapFlag
import org.alter.game.model.Tile
import org.alter.game.model.entity.GameObject
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.rsmod.game.pathfinder.Route
import org.rsmod.game.pathfinder.collision.CollisionStrategies
import org.rsmod.game.pathfinder.collision.CollisionStrategy
import java.util.*
import java.util.ArrayDeque

/**
 * Walk to all the tiles specified in our [path] queue, using [stepType] as
 * the [MovementQueue.StepType].
 *
 * Seems like @detectCollision is not needed any more as we detect it before we search for path all thoooo,
 * could be still used incase when a collision is added during pathing
 */
fun Pawn.walkPath(
    path: Queue<Tile>,
    stepType: MovementQueue.StepType,
    detectCollision: Boolean,
) {
    if (path.isEmpty()) {
        if (this is Player) {
            write(SetMapFlag(255, 255))
        }
        return
    }
    movementQueue.clear()
    var tail: Tile? = null
    var next = path.poll()
    while (next != null) {
        movementQueue.addStep(next, stepType, detectCollision)
        val poll = path.poll()
        if (poll == null) {
            tail = next
        }
        next = poll
    }
    if (tail == null || tail.sameAs(tile)) {
        if (this is Player) {
            write(SetMapFlag(255, 255))
        }
        movementQueue.clear()
        return
    }
    if (this is Player && lastKnownRegionBase != null) {
        write(SetMapFlag(tail.x - lastKnownRegionBase!!.x, tail.z - lastKnownRegionBase!!.z))
    }
}

fun Pawn.walkTo(
    tile: Tile,
    stepType: MovementQueue.StepType = MovementQueue.StepType.NORMAL,
    detectCollision: Boolean = true,
    customCollisionStrategy: CollisionStrategy? = null,
) = walkTo(tile.x, tile.z, stepType, detectCollision, customCollisionStrategy)

fun Pawn.walkTo(
    x: Int,
    z: Int,
    stepType: MovementQueue.StepType = MovementQueue.StepType.NORMAL,
    detectCollision: Boolean = true,
    customCollisionStrategy: CollisionStrategy? = null,
) {
    /*
     * Already standing on requested destination.
     */
    if (tile.x == x && tile.z == z) {
        return
    }

    val route = findRoute(x, z, detectCollision, customCollisionStrategy)
    val tileQueue: Queue<Tile> = ArrayDeque(route.waypoints.map { Tile(it.x, it.z, it.level) })
    this.walkPath(tileQueue, stepType, detectCollision = detectCollision)
}

suspend fun Pawn.walkTo(
    it: QueueTask,
    tile: Tile,
    stepType: MovementQueue.StepType = MovementQueue.StepType.NORMAL,
    detectCollision: Boolean = true,
    customCollisionStrategy: CollisionStrategy? = null,
) = walkTo(it, tile.x, tile.z, stepType, detectCollision, customCollisionStrategy)

suspend fun Pawn.walkTo(
    it: QueueTask,
    x: Int,
    z: Int,
    stepType: MovementQueue.StepType = MovementQueue.StepType.NORMAL,
    detectCollision: Boolean = true,
    customCollisionStrategy: CollisionStrategy? = null,
): Route {
    /*
     * Already standing on requested destination.
     */
    if (tile.x == x && tile.z == z) {
        return Route(Pawn.EMPTY_TILE_DEQUE, alternative = false, success = true)
    }

    val route = findRoute(x, z, detectCollision, customCollisionStrategy)
    movementQueue.clear()

    val tileQueue: Queue<Tile> = ArrayDeque(route.waypoints.map { Tile(it.x, it.z, it.level) })
    this.walkPath(tileQueue, stepType, detectCollision = detectCollision)
    return route
}
private fun Pawn.findRoute(
    x: Int,
    z: Int,
    detectCollision: Boolean,
    customCollisionStrategy: CollisionStrategy? = null,
): Route {
    val collisionStrategy =
        if (detectCollision) {
            customCollisionStrategy ?: CollisionStrategies.Normal
        } else {
            object : CollisionStrategy {
                override fun canMove(
                    tileFlag: Int,
                    blockFlag: Int,
                ): Boolean {
                    return true
                }
            }
        }

    return world.pathFinder.findPath(
        level = this.tile.height,
        srcX = this.tile.x,
        srcZ = this.tile.z,
        destX = x,
        destZ = z,
        srcSize = getSize(),
        collision = collisionStrategy,
    )
}

/**
 * :hmm:
 */
fun Pawn.moveTo(
    x: Int,
    z: Int,
    height: Int = 0,
) {
    tile = Tile(x, z, height)
    movementQueue.clear()

    if (entityType.isNpc) {
        (this as Npc).avatar.teleport(height, x, z, true)
    } else if (entityType.isPlayer) {
        (this as Player).avatar.extendedInfo.setTempMoveSpeed(127)
    }
}
fun Pawn.moveTo(tile: Tile) {
    moveTo(tile.x, tile.z, tile.height)
}

fun Pawn.hasMoveDestination(): Boolean = movementQueue.hasDestination()

fun Pawn.stopMovement() {
    movementQueue.clear()
}
