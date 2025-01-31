package org.alter.plugins.content.commands.commands.developer

import org.alter.api.ext.getCommandArgs
import org.alter.api.ext.message
import org.alter.api.ext.playSong
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.priv.Privilege
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class SongPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("song", Privilege.DEV_POWER, description = "Play song by id") {
            val values = player.getCommandArgs()
            val id = values[0].toInt()
            player.playSong(id)
            player.message("Song: $id")
        }
    }
}
