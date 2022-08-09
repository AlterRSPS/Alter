package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("gfx", Privilege.DEV_POWER, description = "Play gfx") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::gfx 1</col>") { values ->
        val id = values[0].toInt()
        val height = if (values.size >= 2) values[1].toInt() else 100
        player.graphic(id, height)
        player.message("Graphic: $id")
    }
}