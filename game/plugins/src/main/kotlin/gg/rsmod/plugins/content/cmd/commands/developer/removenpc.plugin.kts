package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege

on_command("removenpc", Privilege.DEV_POWER) {
    val chunk = world.chunks.getOrCreate(player.tile)
    val npc = chunk.getEntities<Npc>(player.tile, EntityType.NPC).firstOrNull()
    if (npc != null) {
        world.remove(npc)
    } else {
        player.message("No NPC found in tile.")
    }
}