package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.tryWithUsage

onCommand("gfx", Privilege.DEV_POWER, description = "Play gfx") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::gfx 1</col>") { values ->
        val id = values[0].toInt()
        val height = if (values.size >= 2) values[1].toInt() else 100
        player.graphic(id, height)
        player.message("Graphic: $id")
    }
}
