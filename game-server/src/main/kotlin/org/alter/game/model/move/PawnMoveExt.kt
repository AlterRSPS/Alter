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
    y: Int,
    height: Int = 0,
) {
    tile = Tile(x, y, height)
    movementQueue.clear()

    if (entityType.isNpc) {
        (this as Npc).avatar.teleport(height, x, y, true)
    } else if (entityType.isPlayer) {
        (this as Player).avatar.extendedInfo.setTempMoveSpeed(127)
    }
}
fun Pawn.moveTo(tile: Tile) = moveTo(tile.x, tile.y, tile.height)


/**
 * @property x = xInBuildArea the x coordinate within the build area
 *  to render the map flag at.
 * @property y = zInBuildArea the z coordinate within the build area
 *  to render the map flag at.
 * If nothing gets passed, it will assign x to 255 and z to 255
 * which is used to remove the map flag.
 */
fun Pawn.setMapFlag(
    x: Int = 255,
    y: Int = 255,
) {
    if (this is Player) {
        write(SetMapFlag(x,y))
    }
}

/**
 * Walk to all the tiles specified in our [path] queue, using [stepType] as
 * the [MovementQueue.StepType].
 *
 *
 * Cancel out the walk logic when within @param [target]
 */
fun Pawn.walkPath(
    path: Queue<Tile>,
    stepType: MovementQueue.StepType,
) {
    if (path.isEmpty()) {
        setMapFlag()
        return
    }
    movementQueue.clear()
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
        setMapFlag(tail.x - lastKnownRegionBase!!.x, tail.y - lastKnownRegionBase!!.y)
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
    targetY: Int,
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
        srcZ = tile.y,
        destX = targetX,
        destZ = targetY,
    )
    if (attr[CLIENT_KEY_COMBINATION] == 2 && this is Player && world.privileges.isEligible(privilege, Privilege.ADMIN_POWER)) {
        moveTo(Tile(targetX, targetY, tile.height))
        attr[CLIENT_KEY_COMBINATION] = 0
    } else {
        walkPath(route, stepType)
    }
    return route
}
fun Pawn.walkToInteract(tile: Tile, stepType: MovementQueue.StepType = MovementQueue.StepType.NORMAL) = walkToInteract(targetX = tile.x, targetY = tile.y, stepType = stepType)
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

/**
 * Why Route tho, why not tile when is within range?
 */
suspend fun QueueTask.awaitArrivalRanged(
    route: Route,
    lineOfSightRange: Int,
) : Boolean {
    val pawn = ctx as Pawn
    val destination = pawn.movementQueue.peekLast()
    /**
     * Will be empty if next to the route.
     */
    if (route.isEmpty()) {
        return true
    }
    val tile = route.toTileQueue().last()
    while (true) {
        if (destination != null && pawn.tile.isWithinRadius(destination, lineOfSightRange)) {
            println("This fucker cancels run away : $lineOfSightRange")
            pawn.stopMovement()
            return true
        }
        if (!pawn.movementQueue.hasDestination() && !pawn.tile.isWithinRadius(tile, lineOfSightRange)) {
            return false
        }
        if (pawn.hasMoveDestination()/**destination != null**/) {
            wait(1)
            continue
        } else {
            println("Pawn does not have any move destinations: ${pawn.hasMoveDestination()}")
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
 *
 * @param range How far away till the target. 0 Would be same tile
 */
fun Pawn.pathToInteract(target: Entity, range: Int = 0, tTile: Tile? = null) : Route {
    val targetTile = target.tile
    val route = world.pathFinder.findPath(
        level = tile.height,
        srcX = tile.x,
        srcZ = tile.y,
        destX = targetTile.x,
        destZ = targetTile.y,
        objShape = -1,
        destWidth = 0,
        destHeight = 0,
        moveNear = true,
        collision = CollisionStrategies.Normal,
    )
    return route
}
