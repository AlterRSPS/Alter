package org.alter.game.model.move

import net.rsprot.protocol.game.outgoing.misc.player.SetMapFlag
import org.alter.game.model.Tile
import org.alter.game.model.attr.CLIENT_KEY_COMBINATION
import org.alter.game.model.entity.*
import org.alter.game.model.priv.Privilege
import org.alter.game.model.queue.QueueTask
import org.rsmod.game.pathfinder.Route
import org.rsmod.game.pathfinder.collision.CollisionStrategies
import java.util.*

/**
 * Used to teleport [Pawn] to [Tile],
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
fun Pawn.moveTo(tile: Tile) = moveTo(tile.x, tile.z, tile.height)


/**
 * @property x = xInBuildArea the x coordinate within the build area
 *  to render the map flag at.
 * @property z = zInBuildArea the z coordinate within the build area
 *  to render the map flag at.
 * If nothing gets passed, it will assign x to 255 and z to 255
 * which is used to remove the map flag.
 */
fun Pawn.setMapFlag(
    x: Int = 255,
    z: Int = 255,
) {
    if (this is Player) {
        write(SetMapFlag(x,z))
    }
}

/**
 * Walk to all the tiles specified in our [path] queue, using [stepType] as
 * the [MovementQueue.StepType].
 */
fun Pawn.walkPath(
    path: Queue<Tile>,
    stepType: MovementQueue.StepType,
) {
    if (path.isEmpty()) {
        setMapFlag()
        return
    }
    movementQueue.clear() // @TODO Hmm weird logic (?)
    var tail: Tile? = null
    var next = path.poll()
    while (next != null) {
        movementQueue.addStep(next, stepType)
        val poll = path.poll()
        if (poll == null) {
            tail = next
        }
        next = poll
    }
    /*
     * If the tail is null (should never be unless we mess with code above), or
     * if the tail is the tile we're standing on, then we don't have to move at all!
     */
    if (tail == null || tail.sameAs(tile)) {
        setMapFlag()
        movementQueue.clear()
        return
    }
    if (this is Player && lastKnownRegionBase != null) {
        setMapFlag(tail.x - lastKnownRegionBase!!.x, tail.z - lastKnownRegionBase!!.z)
    }
}
fun Pawn.walkPath(
    path: Route,
    stepType: MovementQueue.StepType,
) = this.walkPath(path.toTileQueue(), stepType)


fun Route.toTileQueue() : Queue<Tile> {
    return ArrayDeque(this.waypoints.map{ Tile(it.x, it.z, it.level) })
}

fun Pawn.stopMovement() {
    movementQueue.clear()
}

fun Pawn.walkToInteract(
    targetX: Int,
    targetZ: Int,
    stepType: MovementQueue.StepType = MovementQueue.StepType.NORMAL,
) : Route {
    if (this is Player) {
        this.closeInterfaceModal()
        this.interruptQueues()
        this.resetInteractions()
    }
    val route = world.pathFinder.findPath(
        level = tile.height,
        srcX = tile.x,
        srcZ = tile.z,
        destX = targetX,
        destZ = targetZ,
    )
    if (attr[CLIENT_KEY_COMBINATION] == 2 && this is Player && world.privileges.isEligible(privilege, Privilege.ADMIN_POWER)) {
        moveTo(Tile(targetX, targetZ, tile.height))
        attr[CLIENT_KEY_COMBINATION] = 0
    } else {
        walkPath(route, stepType)
    }
    return route
}
fun Pawn.walkToInteract(tile: Tile, stepType: MovementQueue.StepType = MovementQueue.StepType.NORMAL) = walkToInteract(targetX = tile.x, targetZ = tile.z, stepType = stepType)
fun Pawn.hasMoveDestination(): Boolean = movementQueue.hasDestination()

suspend fun QueueTask.awaitArrivalInteraction(
    route: Route,
) : Boolean {
    val p = ctx as Player
    val destination = p.movementQueue.peekLast()
    var tile = route.toTileQueue().last()
    while (true) {
        if (!p.movementQueue.hasDestination() && !p.tile.sameAs(tile)) {
            return false
        }
        if (!p.tile.sameAs(tile) && destination != null) {
            wait(1)
            continue
        }
        if (destination == null && !p.tile.sameAs(tile)) {
            return false
        }
        if (p.tile.sameAs(tile)) {
            return true
        }
        break
    }
    return false
}

suspend fun QueueTask.awaitArrivalRanged(
    route: Route,
    lineOfSightRange: Int,
) : Boolean {
    val pawn = ctx as Pawn
    val destination = pawn.movementQueue.peekLast()
    val tile = route.toTileQueue().last()
    val nextTile = pawn.movementQueue.steps.peek().tile
    while (true) {
        /**
         * Player has no move steps nor is within the range.
         */
        if (!pawn.movementQueue.hasDestination() && !pawn.tile.isWithinRadius(tile, lineOfSightRange)) {
            println("failure")
            return false
        }
        if (!pawn.tile.isWithinRadius(tile, lineOfSightRange) && destination != null) {
            wait(1)
            continue
        }
        if (pawn.tile.isWithinRadius(nextTile, lineOfSightRange)) {
            println("Stoped on: ${pawn.tile} : ${nextTile}")
            pawn.stopMovement()
            return true
        }
        break
    }
    return false
}
/**
 * Move / StepTile()
 * That ignore frozen timers.
 * Interactable distance path :
 * Need to look if we can interact with [Entity] within distance [Int]
 * If can: Face it and interact if no, walk to it till it's within Lineofsight view
 *
 * When player gets attacked during his Running it does not stop to retaliate or does it (?)
 */
/**
 * @property pawn
 */
suspend fun Pawn.walkToInteract(
    it: QueueTask,
    target: Entity,
    lineOfSightRange: Int,
): Boolean {
    val sourceSize = getSize()
    val targetSize = when (target) {
        is Player -> getSize()
        is Npc -> getSize()
        is GameObject -> getSize()
        else -> 1
    }
    val targetTile = target.tile
    /**
     * Add validation for Projectiles
     */
    TODO("Fix this shit.")
    val newRoute = world.pathFinder.findPath(
        level = tile.height,
        srcX = tile.x,
        srcZ = tile.z,
        destX = targetTile.x,
        destZ = targetTile.z,
        srcSize = sourceSize,
        objShape = -1,
        destWidth = 3,
        destHeight = 0,
        moveNear = true,
        collision = CollisionStrategies.Normal,
    )
    walkPath(newRoute, MovementQueue.StepType.NORMAL)
    val state = it.awaitArrivalInteraction(newRoute)
    return state
}
