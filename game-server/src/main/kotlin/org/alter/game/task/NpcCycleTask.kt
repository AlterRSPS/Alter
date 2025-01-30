package org.alter.game.task

import org.alter.game.model.World
import org.alter.game.service.GameService

/**
 * A [GameTask] responsible for executing [org.alter.game.model.entity.Npc]
 * cycle logic, sequentially.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class NpcCycleTask : GameTask {
    override fun execute(
        world: World,
        service: GameService,
    ) {
        world.npcs.forEach { n ->
            n.queues.cycle()
            n.cycle()
            n.npcPreSynchronizationTask()
        }
    }
}
