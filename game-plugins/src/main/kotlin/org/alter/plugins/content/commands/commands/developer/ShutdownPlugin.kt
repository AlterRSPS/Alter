package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege
import org.alter.game.model.timer.FORCE_DISCONNECTION_TIMER
import kotlin.system.exitProcess

onCommand(
    "shutdown",
    Privilege.DEV_POWER,
    description = "Shutdown Server expected @param1 Int: How long till shutdown."
) {
    val args = player.getCommandArgs()
    val cycles = args[0].toInt()
    world.queue {
        world.rebootTimer = cycles
        world.sendRebootTimer(cycles)
        wait(cycles)
        world.players.forEach { player -> player.timers[FORCE_DISCONNECTION_TIMER] = 0 }
        wait(5)
        exitProcess(0)
    }
}
