package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege

on_command("removeobj", Privilege.DEV_POWER, description = "Remove object under player") {
    val chunk = world.chunks.getOrCreate(player.tile)
    val obj = chunk.getEntities<GameObject>(player.tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).firstOrNull()
    if (obj != null) {
        world.remove(obj)
    } else {
        player.message("No object found in tile.")
    }
}