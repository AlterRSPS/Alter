package org.alter.plugins.content.commands.commands.admin

import org.alter.api.ext.getCommandArgs
import org.alter.api.ext.message
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.priv.Privilege
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class TransmogPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("transmog", Privilege.ADMIN_POWER, description = "Transmog yourself") {
            val args = player.getCommandArgs()
            val id = args[0].toInt()
            player.setTransmogId(id)
            player.message("It's morphing time!")
        }
    }
}
