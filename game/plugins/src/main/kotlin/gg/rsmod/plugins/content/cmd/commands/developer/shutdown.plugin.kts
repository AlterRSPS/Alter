package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.game.model.timer.FORCE_DISCONNECTION_TIMER
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage
import kotlin.system.exitProcess

on_command("shutdown", Privilege.DEV_POWER, description = "Shutdown Server") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::shutdown 500</col>"){ values ->
        val cycles = values[0].toInt()
        world.queue {
            world.rebootTimer = cycles
            world.sendRebootTimer(cycles)
            wait(cycles)
            world.players.forEach { player -> player.timers[FORCE_DISCONNECTION_TIMER] = 0 }
            wait(5)
            exitProcess(0)
        }
    }
}