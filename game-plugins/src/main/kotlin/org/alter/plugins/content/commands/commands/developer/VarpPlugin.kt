package org.alter.plugins.content.commands.commands.developer

import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.priv.Privilege
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class VarpPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("varp", Privilege.DEV_POWER, description = "Set varp to amount") {
            val args = player.getCommandArgs()
            val varp = args[0].toInt()
            val state = args[1].toInt()
            val oldState = player.getVarp(varp)
            player.setVarp(varp, state)
            player.message("Set varp (<col=801700>$varp</col>) from <col=801700>$oldState</col> to <col=801700>${player.getVarp(varp)}</col>")
        }

        onCommand("getvarp") {
            val args = player.getCommandArgs()
            val varpState = player.getVarp(args[0].toInt())
            player.message("${args[0]} Varp state: $varpState")
        }
    }
}
