package org.alter.game.task

import org.alter.game.model.World
import org.alter.game.service.GameService

/**
 * A [GameTask] is anything that can be scheduled to be executed on the
 * game-thread.
 *
 * @author Tom <rspsmods@gmail.com>
 */
interface GameTask {
    /**
     * Executes the [GameTask] logic.
     */
    fun execute(
        world: World,
        service: GameService,
    )
}
