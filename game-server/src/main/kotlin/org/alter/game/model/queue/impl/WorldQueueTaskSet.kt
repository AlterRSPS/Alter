package org.alter.game.model.queue.impl

import org.alter.game.model.queue.QueueTaskSet
import kotlin.coroutines.resume

/**
 * A [QueueTaskSet] implementation for [org.alter.game.model.World].
 * All [org.alter.game.model.queue.QueueTask]s are handled every tick.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class WorldQueueTaskSet : QueueTaskSet() {
    override fun cycle() {
        val iterator = queue.iterator()
        while (iterator.hasNext()) {
            val task = iterator.next()

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
                iterator.remove()
            }
        }
    }
}
