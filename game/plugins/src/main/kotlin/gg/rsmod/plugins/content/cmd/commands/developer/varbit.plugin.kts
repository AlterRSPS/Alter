package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("varbit", Privilege.DEV_POWER, description = "Set varbit to amount") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::varbit 5451 1</col>") { values ->
        val varbit = values[0].toInt()
        val state = values[1].toInt()
        val oldState = player.getVarbit(varbit)
        player.setVarbit(varbit, state)
        player.message("Set varbit (<col=801700>$varbit</col>) from <col=801700>$oldState</col> to <col=801700>${player.getVarbit(varbit)}</col>")
    }
}