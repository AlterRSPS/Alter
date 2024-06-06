package gg.rsmod.net.codec.login.org.alter.game.task.sequential

import org.alter.game.model.World
import org.alter.game.service.GameService
import org.alter.game.task.GameTask

/**
 * Updates the coords for all players within the rsprot library. This is run after processing to properly account for
 * displacement effects [dspear, etc]
 */
class SequentialPlayerCoordCycleTask : GameTask {

    override fun execute(world: World, service: GameService) {
        world.players.forEach { p ->
            val start = System.currentTimeMillis()
            p.playerInfo.updateCoord(p.tile.height, p.tile.x, p.tile.z)
            p.npcInfo.updateCoord(-1, p.tile.height, p.tile.x, p.tile.z)
            p.worldEntityInfo.updateCoord(-1, p.tile.height, p.tile.x, p.tile.z)


            /*
             * Log the time it takes for task to handle the player's cycle
             * logic.
             */
            val time = System.currentTimeMillis() - start
            service.playerTimes.merge(p.username, time) { _, oldTime -> oldTime + time }
        }
    }
}