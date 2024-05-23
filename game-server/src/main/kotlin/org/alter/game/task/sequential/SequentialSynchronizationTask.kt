package org.alter.game.task.sequential

import org.alter.game.model.World
import org.alter.game.service.GameService
import org.alter.game.task.GameTask

/**
 * A [GameTask] that is responsible for sending [org.alter.game.model.entity.Pawn]
 * data to [org.alter.game.model.entity.Pawn]s.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class SequentialSynchronizationTask : GameTask {

    override fun execute(world: World, service: GameService) {
        world.network.playerInfoProtocol.update()
        world.network.npcInfoProtocol.update()
    }
}