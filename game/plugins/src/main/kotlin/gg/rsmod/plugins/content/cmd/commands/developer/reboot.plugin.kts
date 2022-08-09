package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("reboot", Privilege.DEV_POWER, description = "Restart Server") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::reboot 500</col>") { values ->
        val cycles = values[0].toInt()
        world.rebootTimer = cycles
        world.sendRebootTimer()
    }
}