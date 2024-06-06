package org.alter.game.task.sequential

import org.alter.game.model.World
import org.alter.game.service.GameService
import org.alter.game.task.GameTask

/**
 * A [GameTask] responsible for executing [org.alter.game.model.entity.Pawn]
 * "post" cycle logic, sequentially. Post cycle means that the this task
 * will be handled near the end of the cycle, after the synchronization
 * tasks.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class SequentialPlayerPostCycleTask : GameTask {
    override fun execute(
        world: World,
        service: GameService,
    ) {
        world.players.forEach { p ->
            p.postCycle()
        }
    }
}
