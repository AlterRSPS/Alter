package org.alter.plugins.content.commands.commands.developer

import org.alter.api.ext.getCommandArgs
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.move.moveTo
import org.alter.game.model.priv.Privilege
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class TelerPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("teler", Privilege.DEV_POWER, description = "Teleport to region") {
            val args = player.getCommandArgs()
            val region = args[0].toInt()
            val tile = Tile.fromRegion(region)
            player.moveTo(tile)
        }
    }
}
