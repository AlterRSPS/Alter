package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("getvarbit", Privilege.DEV_POWER, description = "Get varbit state") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::getvarbit 5451</col>") { values ->
        val varbit = values[0].toInt()
        val state = player.getVarbit(varbit)
        player.message("Get varbit (<col=801700>$varbit</col>): <col=801700>$state</col>")
    }
}