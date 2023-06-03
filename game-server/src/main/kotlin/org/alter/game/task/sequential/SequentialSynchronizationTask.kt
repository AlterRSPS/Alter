package org.alter.game.task.sequential

import org.alter.game.model.World
import org.alter.game.service.GameService
import org.alter.game.sync.task.*
import org.alter.game.task.GameTask

/**
 * A [GameTask] that is responsible for sending [gg.rsmod.game.model.entity.Pawn]
 * data to [gg.rsmod.game.model.entity.Pawn]s.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class SequentialSynchronizationTask : GameTask {

    override fun execute(world: World, service: GameService) {
        val worldPlayers = world.players
        val worldNpcs = world.npcs
        val rawNpcs = world.npcs.entries
        val npcSync = NpcSynchronizationTask(rawNpcs)

        worldPlayers.forEach { p ->
            PlayerPreSynchronizationTask.run(p)
        }

        for (n in worldNpcs.entries) {
            if (n != null) {
                NpcPreSynchronizationTask.run(n)
            }
        }

        worldPlayers.forEach { p ->
            /*
             * Non-human [gg.rsmod.game.model.entity.Player]s do not need this
             * to send any synchronization data to their game-client as they do
             * not have one.
             */
            if (p.entityType.isHumanControlled && p.initiated) {
                PlayerSynchronizationTask.run(p)
            }
        }

        worldPlayers.forEach { p ->
            /*
             * Non-human [gg.rsmod.game.model.entity.Player]s do not need this
             * to send any synchronization data to their game-client as they do
             * not have one.
             */
            if (p.entityType.isHumanControlled && p.initiated) {
                npcSync.run(p)
            }
        }

        worldPlayers.forEach { p ->
            PlayerPostSynchronizationTask.run(p)
        }

        for (n in worldNpcs.entries) {
            if (n != null) {
                NpcPostSynchronizationTask.run(n)
            }
        }
    }
}