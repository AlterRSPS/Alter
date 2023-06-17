package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

on_command("jingle", Privilege.DEV_POWER, description = "Play jingle by id") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::jingle 1</col>") { values ->
        val id = values[0].toInt()
        player.playJingle(id)
        player.message("Jingle: $id")
    }
}