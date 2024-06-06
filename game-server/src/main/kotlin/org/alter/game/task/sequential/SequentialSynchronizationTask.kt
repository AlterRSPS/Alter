package org.alter.game.task.sequential

import net.rsprot.protocol.game.outgoing.info.npcinfo.SetNpcUpdateOrigin
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
    override fun execute(
        world: World,
        service: GameService,
    ) {
        val worldPlayers = world.players
        val worldNpcs = world.npcs

        worldPlayers.forEach { p ->
            PlayerPreSynchronizationTask.run(p)
        }

        for (n in worldNpcs.entries) {
            if (n != null) {
                NpcPreSynchronizationTask.run(n)
            }
        }

        world.network.worldEntityInfoProtocol.update()

        // First off, write the world entity info to the client - it must
        // be aware of the updates before receiving the rebuild world entity packets
        world.players.forEach {
            if (it.entityType.isHumanControlled && it.initiated) {
                it.write(it.worldEntityInfo.toPacket()) // try-catch it, this _can_ throw exceptions during .toPacket()
            }
        }

        world.players.forEach {
            if (it.entityType.isHumanControlled && it.initiated) {
                // If the player is not on a dynamic world entity, we can set the
                // origin point as the local player coordinate
                val (x, z, level) = it.tile
                it.npcInfo.updateCoord(-1, level, x, z)
                it.playerInfo.updateRenderCoord(-1, level, x, z)
            }
        }

        world.network.playerInfoProtocol.update()
        world.network.npcInfoProtocol.update()

        world.players.forEach {
            /*
             * Non-human [org.alter.game.model.entity.Player]s do not need this
             * to send any synchronization data to their game-client as they do
             * not have one.
             */
            if (it.entityType.isHumanControlled && it.initiated) {
                it.write(it.playerInfo.toPacket(-1)) // try-catch it, this _can_ throw exceptions during .toPacket()
                it.write(
                    SetNpcUpdateOrigin(
                        it.tile.x - (it.buildArea!!.zoneX shl 3),
                        it.tile.z - (it.buildArea!!.zoneZ shl 3),
                    ),
                )
                it.write(it.npcInfo.toNpcInfoPacket(-1))
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
