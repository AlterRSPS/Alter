package org.alter.game.task

import org.alter.game.model.World
import org.alter.game.service.GameService

/**
 * A [GameTask] responsible for executing [org.alter.game.model.entity.Player]
 * cycle logic, sequentially.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerCycleTask : GameTask {
    override fun execute(
        world: World,
        service: GameService,
    ) {
        world.players.forEach { p ->
            val start = System.currentTimeMillis()
            p.queues.cycle()
            p.cycle()
            p.playerPreSynchronizationTask()
            /*
             * Log the time it takes for task to handle the player's cycle
             * logic.
             */
            val time = System.currentTimeMillis() - start
            service.playerTimes.merge(p.username, time) { _, oldTime -> oldTime + time }
        }
    }
}
