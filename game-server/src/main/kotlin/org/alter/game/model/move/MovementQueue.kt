package org.alter.game.model.move

import org.alter.game.info.MovementType
import org.alter.game.info.PlayerInfo
import org.alter.game.model.Direction
import org.alter.game.model.EntityType
import org.alter.game.model.Tile
import org.alter.game.model.move.MovementQueue.Step
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import java.util.*
import kotlin.math.abs

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

    fun cycle() {
        var next = steps.poll()
        val pathSize = steps.size
        if (next != null) {
            var tile = pawn.tile
            var walkDirection: Direction?
            var runDirection: Direction? = null
            walkDirection = Direction.between(tile, next.tile)
            if (walkDirection != Direction.NONE &&
                (pawn.world.canTraverse(tile, walkDirection, pawn))
            ) {
                if (pawn is Npc) {
                    val entitiesClipped = mutableListOf<Pawn>()

                    pawn.world.chunks
                        .get(next.tile, createIfNeeded = true)!!
                        .getEntities<Npc>(next.tile, EntityType.NPC)
                        .filter { it.tile == next.tile }
                        .let { entitiesClipped.addAll(it) }

                    pawn.world.chunks
                        .get(next.tile, createIfNeeded = true)!!
                        .getEntities<Player>(next.tile, EntityType.CLIENT)
                        .filter { it.tile == next.tile }
                        .let { entitiesClipped.addAll(it) }

                    if (entitiesClipped.isNotEmpty()) {
                        entitiesClipped.clear()
                        clear()
                        return
                    }
                }
                tile = Tile(next.tile)
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
                            tile = Tile(next.tile)
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
                pawn.tile = Tile(tile)
                if (pawn is Player) {
                    PlayerInfo(pawn).setMoveSpeed(if (pawn.isRunning() && pathSize > 0) MovementType.RUN else MovementType.WALK)
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
        var dz = next.z - current.z
        val delta = Math.max(abs(dx), abs(dz))

        for (i in 0 until delta) {
            if (dx < 0) {
                dx++
            } else if (dx > 0) {
                dx--
            }

            if (dz < 0) {
                dz++
            } else if (dz > 0) {
                dz--
            }

            val step = next.transform(-dx, -dz)
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
