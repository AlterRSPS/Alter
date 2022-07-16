package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("getid", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example for proper command <col=801700>::getid Item/Npc/Object Keyword </col>") { values ->

    }
}