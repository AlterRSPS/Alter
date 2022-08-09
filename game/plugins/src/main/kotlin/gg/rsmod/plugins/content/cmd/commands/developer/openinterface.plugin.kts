package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("openinterface", Privilege.DEV_POWER, description = "Open interface by id") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::openinterface interfId parentId pChildId clickThrough isModal</col>") { values ->
        val component = values[0].toInt()
        val parent = values[1].toIntOrNull() ?: getDisplayComponentId(player.interfaces.displayMode)
        val child = values[2].toInt()
        var clickable = values[3].toIntOrNull() ?: 0
        clickable = if(clickable != 1) 0 else 1
        val modal = values[4].toIntOrNull() ?: 1 == 1
        player.openInterface(parent, child, component, clickable, isModal = modal)
        player.message("Opening interface <col=801700>$component</col> on <col=0000ff>$parent:$child</col>")
    }
}