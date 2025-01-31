package org.alter.plugins.content.commands.commands.developer

import org.alter.api.ext.getCommandArgs
import org.alter.api.ext.message
import org.alter.api.ext.playSound
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.priv.Privilege
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class SoundPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("sound", Privilege.DEV_POWER, description = "Play sound by id") {
            val values = player.getCommandArgs()
            val id = values[0].toInt()
            player.playSound(id)
            player.message("Sound: $id")
        }
    }
}
