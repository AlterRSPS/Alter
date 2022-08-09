package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege

on_command("removenpc", Privilege.DEV_POWER, description = "Remove Npc under player") {
    val chunk = world.chunks.getOrCreate(player.tile)
    val npc = chunk.getEntities<Npc>(player.tile, EntityType.NPC).firstOrNull()
    if (npc != null) {
        world.remove(npc)
    } else {
        player.message("No NPC found in tile.")
    }
}