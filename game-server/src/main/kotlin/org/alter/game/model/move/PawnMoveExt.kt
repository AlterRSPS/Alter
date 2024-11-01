package org.alter.game.model.move

import net.rsprot.protocol.game.outgoing.misc.player.SetMapFlag
import org.alter.game.model.LockState
import org.alter.game.model.Tile
import org.alter.game.model.attr.CLIENT_KEY_COMBINATION
import org.alter.game.model.entity.*
import org.alter.game.model.move.MovementQueue.StepType
import org.alter.game.model.priv.Privilege
import org.rsmod.game.pathfinder.Route
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
fun Pawn.moveTo(tile: Tile) = moveTo(tile.x, tile.z, tile.height)


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
    path: Route,
    stepType: StepType,
) {
    walkPath(path.toTileQueue(), stepType)
}
fun Pawn.walkPath(
    path: Queue<Tile>,
    stepType: StepType,
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
        setMapFlag(tail.x - lastKnownRegionBase!!.x, tail.z - lastKnownRegionBase!!.z)
    }
}

fun Route.toTileQueue() : Queue<Tile> {
    return ArrayDeque(this.waypoints.map{ Tile(it.x, it.z, it.level) })
}

fun Pawn.stopMovement() = movementQueue.clear()

fun Pawn.walkTo(
    targetX: Int,
    targetY: Int,
    stepType: StepType = StepType.NORMAL,
) {
    if (this is Player) {
        if (!lock.canMove()) {
            writeMessage("You are locked")
            return
        }
        this.closeInterfaceModal()
        this.interruptQueues()
        this.resetInteractions()
    }
    val route = world.pathFinder.findPath(
        level = tile.height,
        srcX = tile.x,
        srcZ = tile.z,
        destX = targetX,
        destZ = targetY,
    )
    if (attr[CLIENT_KEY_COMBINATION] == 2 && this is Player && world.privileges.isEligible(privilege, Privilege.ADMIN_POWER)) {
        moveTo(Tile(targetX, targetY, tile.height))
        attr[CLIENT_KEY_COMBINATION] = 0
    } else {
        walkPath(route.toTileQueue(), stepType)
    }
}
fun Pawn.walkTo(tile: Tile, stepType: StepType = StepType.NORMAL) = walkTo(targetX = tile.x, targetY = tile.z, stepType = stepType)

fun Pawn.hasMoveDestination(): Boolean = movementQueue.hasDestination()