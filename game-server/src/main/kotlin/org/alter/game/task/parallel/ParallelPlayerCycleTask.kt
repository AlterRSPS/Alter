package org.alter.game.task.parallel

import org.alter.game.model.World
import org.alter.game.service.GameService
import org.alter.game.task.GameTask
import gg.rsmod.util.concurrency.PhasedTask
import java.util.concurrent.ExecutorService
import java.util.concurrent.Phaser

/**
 * A [GameTask] responsible for executing [org.alter.game.model.entity.Pawn]
 * cycle logic, in parallel.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ParallelPlayerCycleTask(private val executor: ExecutorService) : GameTask {

    private val phaser = Phaser(1)

    override fun execute(world: World, service: GameService) {
        val worldPlayers = world.players
        val playerCount = worldPlayers.count()

        phaser.bulkRegister(playerCount)
        worldPlayers.forEach { p ->
            executor.execute {
                PhasedTask.run(phaser) {
                    p.cycle()
                }
            }
        }
        phaser.arriveAndAwaitAdvance()
    }
}