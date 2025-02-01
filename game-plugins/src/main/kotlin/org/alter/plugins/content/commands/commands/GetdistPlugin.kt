package org.alter.plugins.content.commands.commands

import org.alter.api.ext.getCommandArgs
import org.alter.api.ext.message
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class GetdistPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("getdist") {
            val args = player.getCommandArgs()
            val x = args[0].toInt()
            val z = args[1].toInt()
            val tile = Tile(x,z)
            player.message("Player:[${player.tile}], Destination: [${tile}], Distance: [${player.tile.getDistance(tile)}]" )
        }
    }
}
