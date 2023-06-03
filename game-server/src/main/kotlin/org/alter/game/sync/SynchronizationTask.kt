package org.alter.game.sync

import org.alter.game.model.entity.Pawn

/**
 * A task in any pawn synchronization process.
 *
 * @author Tom <rspsmods@gmail.com>
 */
interface SynchronizationTask<T : Pawn> {

    fun run(pawn: T)
}