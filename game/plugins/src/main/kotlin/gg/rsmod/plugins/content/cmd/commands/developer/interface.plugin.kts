package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("interface", Privilege.DEV_POWER, description = "Open interface by id") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::interface 214</col>") { values ->
        val component = values[0].toInt()
        player.openInterface(component, InterfaceDestination.MAIN_SCREEN)
        player.message("Opening interface <col=801700>$component</col>")
    }
}