package org.alter.game.sync.task

import org.alter.game.model.Tile
import org.alter.game.model.entity.Npc
import org.alter.game.sync.SynchronizationTask

/**
 * @author Tom <rspsmods@gmail.com>
 */
object NpcPostSynchronizationTask : SynchronizationTask<Npc> {

    override fun run(pawn: Npc) {
        val oldTile = pawn.lastTile
        val moved = oldTile == null || !oldTile.sameAs(pawn.tile)

        if (moved) {
            pawn.lastTile = Tile(pawn.tile)
        }
        pawn.moved = false
        pawn.steps = null
//        pawn.blockBuffer.clean()
    }
}