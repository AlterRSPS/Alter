package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("varp", Privilege.DEV_POWER, description = "Set varp to amount") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::varp 173 1</col>") { values ->
        val varp = values[0].toInt()
        val state = values[1].toInt()
        val oldState = player.getVarp(varp)
        player.setVarp(varp, state)
        player.message("Set varp (<col=801700>$varp</col>) from <col=801700>$oldState</col> to <col=801700>${player.getVarp(varp)}</col>")
    }
}