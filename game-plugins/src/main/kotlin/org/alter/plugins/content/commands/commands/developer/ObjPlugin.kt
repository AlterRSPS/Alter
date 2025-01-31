package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege

onCommand("obj", Privilege.DEV_POWER, description = "Spawn object by id") {
    val values = player.getCommandArgs()
    val id = values[0].toInt()
    val type = if (values.size > 1) values[1].toInt() else 10
    val rot = if (values.size > 2) values[2].toInt() else 0
    val obj = DynamicObject(id, type, rot, player.tile)
    player.message("Adding object to: ")
    world.spawn(obj)
}
