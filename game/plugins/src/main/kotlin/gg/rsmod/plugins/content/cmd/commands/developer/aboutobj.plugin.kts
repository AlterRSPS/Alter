package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege

on_command("aboutobj", Privilege.DEV_POWER, description = "Object information") {
    val chunk = world.chunks.getOrCreate(player.tile)
    val obj = chunk.getEntities<GameObject>(player.tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).firstOrNull()
    if (obj != null) {
        player.message("obj [id = ${obj.id}, type = ${obj.type}, rot = ${obj.rot}]")
    } else {
        player.message("No object found in tile.")
    }
}