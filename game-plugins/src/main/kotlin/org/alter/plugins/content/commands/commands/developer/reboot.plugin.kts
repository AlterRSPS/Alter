package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

on_command("reboot", Privilege.DEV_POWER, description = "Restart Server") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::reboot 500</col>") { values ->
        val cycles = values[0].toInt()
        world.rebootTimer = cycles
        world.sendRebootTimer()
    }
}