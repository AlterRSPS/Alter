package org.alter.game.model.move

import org.alter.game.info.NpcInfo
import org.alter.game.info.PlayerInfo
import org.alter.game.model.Direction
import org.alter.game.model.EntityType
import org.alter.game.model.Tile
import org.alter.game.model.attr.FACING_PAWN_ATTR
import org.alter.game.model.entity.Npc
import org.alter.game.model.move.MovementQueue.Step
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.rsmod.routefinder.RouteFinding
import org.rsmod.routefinder.collision.CollisionStrategy
import java.util.*
import kotlin.math.abs
import kotlin.math.sign

/**
 * Responsible for handling a queue of [Step]s for a [Pawn].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class MovementQueue(val pawn: Pawn) {
    /**
     * A [Deque] of steps.
     */
    private val steps: Deque<Step> = ArrayDeque()

    /**
     * If any step is queued.
     */
    fun hasDestination(): Boolean = steps.isNotEmpty()

    /**
     * Get the last tile in our [steps] without removing it.
     */
    fun peekLast(): Tile? = peekLastStep()?.tile

    fun peekLastStep(): Step? = if (steps.isNotEmpty()) steps.peekLast() else null

    fun clear() {
        steps.clear()
    }

    fun addStep(
        step: Tile,
        type: StepType,
    ) {
        val current = if (steps.any()) steps.peekLast().tile else pawn.tile
        addStep(current, step, type)
    }

    /**
     * @TODO
     */
    fun nextStep(step: Tile): Step? {
        if (!this.hasDestination()) return null
        // val current = if (steps.any()) steps.peekLast().tile else pawn.tile
        val curX = pawn.tile.x
        val curY = pawn.tile.z

        val destX = step.x
        val destY = step.z

        if (curX != destX || curY != destY) {
            val dx = (destX - curX).sign
            val dy = (destY - curY).sign
            // if (canStep(dx, dy)) return Step(curX + dx, curY + dy)
        }
        return null
    }


    fun canStep(tile: Tile,
                srcSize: Int = 1,
                collision: CollisionStrategy = CollisionStrategy.Normal
    ) = canStep(
        tile.x,
        tile.z,
        srcSize,
        collision
    )

    fun canStep(
        dx: Int,
        dy: Int,
        srcSize: Int = 1,
        collision: CollisionStrategy = CollisionStrategy.Normal
    ): Boolean {
        return pawn.world.stepValidator.canTravel(
            level = pawn.tile.height,
            x = pawn.tile.x,
            z = pawn.tile.z,
            offsetX = dx,
            offsetZ = dy,
            size = srcSize,
            collision = collision
        )
    }
    /**
     * @TODO Add support for crawling. And have rule-set implemented for npc travel
     */
    fun cycle() {
        var next = steps.poll()
        val pathSize = steps.size
        if (next != null) {
            var tile = pawn.tile
            val tail = pawn.tile
            var walkDirection: Direction?
            var runDirection: Direction? = null
            walkDirection = Direction.between(tile, next.tile)
            if (walkDirection != Direction.NONE &&
                (pawn.world.canTraverse(tile, walkDirection, pawn))
            ) {
                tile = next.tile
                pawn.lastFacingDirection = walkDirection
                val running =
                    when (next.type) {
                        StepType.NORMAL -> pawn.isRunning()
                        StepType.FORCED_RUN -> true
                        StepType.FORCED_WALK -> false
                    }
                if (running) {
                    next = steps.poll()
                    if (next != null) {
                        runDirection = Direction.between(tile, next.tile)
                        if (pawn.world.canTraverse(tile, runDirection, pawn)) {
                            tile = next.tile
                            pawn.lastFacingDirection = runDirection
                        } else {
                            clear()
                            runDirection = null
                        }
                    }
                }
            } else {
                walkDirection = null
                clear()
            }
            if (walkDirection != null && walkDirection != Direction.NONE) {
                pawn.steps = StepDirection(walkDirection, runDirection)
                pawn.tile = tile
                if (pawn is Player) {
                    PlayerInfo(pawn).setMoveSpeed(if (pawn.isRunning() && pathSize > 0) MovementType.RUN else MovementType.WALK)
                }
                if (pawn.entityType.isNpc) {
                    pawn as Npc
                    NpcInfo(pawn).walk(tile.x - tail.x, tile.z - tail.z)
                }
            }
        }
    }
    private fun addStep(
        current: Tile,
        next: Tile,
        type: StepType,
    ) {
        var dx = next.x - current.x
        var dy = next.z - current.z
        val delta = Math.max(abs(dx), abs(dy))

        for (i in 0 until delta) {
            if (dx < 0) {
                dx++
            } else if (dx > 0) {
                dx--
            }

            if (dy < 0) {
                dy++
            } else if (dy > 0) {
                dy--
            }

            val step = next.transform(-dx, -dy)
            steps.add(Step(step, type))
        }
    }

    data class StepDirection(val walkDirection: Direction?, val runDirection: Direction?)

    data class Step(val tile: Tile, val type: StepType)

    enum class StepType {
        NORMAL,
        FORCED_WALK,
        FORCED_RUN,
    }
}
