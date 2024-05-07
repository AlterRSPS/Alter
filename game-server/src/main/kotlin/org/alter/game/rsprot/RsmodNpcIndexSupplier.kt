package org.alter.game.rsprot

import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcIndexSupplier
import org.alter.game.model.World
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Player

class RsmodNpcIndexSupplier(val world: World): NpcIndexSupplier {

    override fun supply(localPlayerIndex: Int, level: Int, x: Int, z: Int, viewDistance: Int): Iterator<Int> {
        val player = world.players[localPlayerIndex] ?: error("Player not found at index: $localPlayerIndex")

        return world.npcs.entries.mapIndexedNotNull { index, npc ->
            if (npc != null && npc.tile.height == level && shouldAdd(player, npc, viewDistance)) index else null
        }.toList().iterator()
    }
}

private fun shouldAdd(player: Player, npc: Npc, viewDistance: Int): Boolean =
    npc.isSpawned() && !npc.invisible && npc.tile.isWithinRadius(
        player.tile,
        viewDistance
    ) && (npc.owner == null || npc.owner == player)

