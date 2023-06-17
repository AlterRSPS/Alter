package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

on_command("sound", Privilege.DEV_POWER, description = "Play sound by id") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::sound 1</col>") { values ->
        val id = values[0].toInt()
        player.playSound(id)
        player.message("Sound: $id")
    }
}
