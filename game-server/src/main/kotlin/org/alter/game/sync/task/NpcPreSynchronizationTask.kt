package org.alter.game.sync.task

import org.alter.game.model.entity.Npc
import org.alter.game.sync.SynchronizationTask

/**
 * @author Tom <rspsmods@gmail.com>
 */
object NpcPreSynchronizationTask : SynchronizationTask<Npc> {

    override fun run(pawn: Npc) {
        pawn.movementQueue.cycle()
    }
}