package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege

onCommand("gfx", Privilege.DEV_POWER, description = "Play gfx") {
    val values = player.getCommandArgs()
    val id = values[0].toInt()
    val height = if (values.size >= 2) values[1].toInt() else 100
    player.graphic(id, height)
    player.message("Graphic: $id")
}
