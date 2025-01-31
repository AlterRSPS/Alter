package org.alter.plugins.content.commands.commands.developer

import org.alter.api.ext.getCommandArgs
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.priv.Privilege
import org.alter.game.model.timer.FORCE_DISCONNECTION_TIMER
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import kotlin.system.exitProcess

class ShutdownPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
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
    }
}
