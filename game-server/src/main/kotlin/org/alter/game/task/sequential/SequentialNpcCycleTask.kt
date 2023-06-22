package org.alter.game.task.sequential

import org.alter.game.model.World
import org.alter.game.service.GameService
import org.alter.game.task.GameTask

/**
 * A [GameTask] responsible for executing [org.alter.game.model.entity.Npc]
 * cycle logic, sequentially.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class SequentialNpcCycleTask : GameTask {

    override fun execute(world: World, service: GameService) {
        world.npcs.forEach { n ->
            n.cycle()
        }
    }
}