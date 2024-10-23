package org.alter.game.rsprot

import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcIndexSupplier
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Player

class RsmodNpcIndexSupplier(val world: World) : NpcIndexSupplier {
    override fun supply(
        localPlayerIndex: Int,
        level: Int,
        x: Int,
        y: Int,
        viewDistance: Int,
    ): Iterator<Int> {
        val player = world.players[localPlayerIndex] ?: error("Player not found at index: $localPlayerIndex")
        val tile = Tile(x, y, level)
        val chunk = world.chunks.get(tile) ?: error("Invalid chunk for : $tile")

        val surrounding = chunk.coords.getSurroundingCoords()

        return surrounding
            .mapNotNull { world.chunks.get(it, createIfNeeded = false) }
            .flatMap { it.npcs }
            .filter { shouldAdd(player, it, viewDistance) }
            .map { it.index }.iterator()
    }
}

private fun shouldAdd(
    player: Player,
    npc: Npc,
    viewDistance: Int,
): Boolean =
    npc.isSpawned() && !npc.invisible &&
        npc.tile.isWithinRadius(
            player.tile,
            viewDistance,
        ) && (npc.owner == null || npc.owner == player)
