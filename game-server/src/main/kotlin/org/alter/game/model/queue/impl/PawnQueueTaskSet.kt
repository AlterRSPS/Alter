package org.alter.game.model.queue.impl

import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTaskSet
import org.alter.game.model.queue.TaskPriority
import kotlin.coroutines.resume

/**
 * A [QueueTaskSet] implementation for [org.alter.game.model.entity.Pawn]s.
 * Each [org.alter.game.model.queue.QueueTask] is handled one at a time.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PawnQueueTaskSet : QueueTaskSet() {
    override fun cycle() {
        while (true) {
            val task = queue.peekFirst() ?: break

            if (task.priority == TaskPriority.STANDARD && task.ctx is Player && task.ctx.hasMenuOpen()) {
                break
            }

            if (!task.invoked) {
                task.invoked = true
                task.coroutine.resume(Unit)
            }

            task.cycle()

            if (!task.suspended()) {
                /*
                 * Task is no longer in a suspended state, which means its job is
                 * complete.
                 */
                queue.remove(task)
                /*
                 * Since this task is complete, let's handle any upcoming
                 * task now instead of waiting until next cycle.
                 */
                continue
            }
            break
        }
    }

    private fun Player.hasMenuOpen(): Boolean = world.plugins.isMenuOpened(this)
}
