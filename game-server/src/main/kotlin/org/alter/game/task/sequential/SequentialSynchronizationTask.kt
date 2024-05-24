package org.alter.game.task.sequential

import org.alter.game.model.World
import org.alter.game.service.GameService
import org.alter.game.sync.task.NpcPostSynchronizationTask
import org.alter.game.sync.task.NpcPreSynchronizationTask
import org.alter.game.sync.task.PlayerPostSynchronizationTask
import org.alter.game.sync.task.PlayerPreSynchronizationTask
import org.alter.game.task.GameTask

/**
 * A [GameTask] that is responsible for sending [org.alter.game.model.entity.Pawn]
 * data to [org.alter.game.model.entity.Pawn]s.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class SequentialSynchronizationTask : GameTask {

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun execute(world: World, service: GameService) {
        val worldPlayers = world.players
        val worldNpcs = world.npcs
        val rawNpcs = world.npcs.entries

        worldPlayers.forEach { p ->
            PlayerPreSynchronizationTask.run(p)
        }

        for (n in worldNpcs.entries) {
            if (n != null) {
                NpcPreSynchronizationTask.run(n)
            }
        }

        world.network.playerInfoProtocol.update()
        world.players.forEach {
            if (it.entityType.isHumanControlled && it.initiated) {
                it.write(it.playerInfo.toPacket()) // try-catch it, this _can_ throw exceptions during .toPacket()
            }
        }
        world.network.npcInfoProtocol.update()
        worldPlayers.forEach { p ->
            /*
             * Non-human [org.alter.game.model.entity.Player]s do not need this
             * to send any synchronization data to their game-client as they do
             * not have one.
             */
            if (p.entityType.isHumanControlled && p.initiated) {
                p.write(p.npcInfo.toNpcInfoPacket())
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