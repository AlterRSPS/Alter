package org.alter.plugins.content.commands.commands.admin

import org.alter.api.ext.message
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.priv.Privilege
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class MyposPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {

        arrayOf("mypos", "coords", "pos").forEach { pos ->

            onCommand(pos, Privilege.ADMIN_POWER, description = "Your position in coordinates") {
                val instancedMap = world.instanceAllocator.getMap(player.tile)
                val tile = player.tile
                if (instancedMap == null) {
                    player.message("Tile=[<col=801700>x: ${tile.x}, y: ${tile.z}, height: ${tile.height}</col>], Region=${player.tile.regionId}")
                } else {
                    val delta = tile - instancedMap.area.bottomLeft
                    player.message("Tile=[<col=801700>x: ${tile.x}, y: ${tile.z}, height: ${tile.height}</col>], Relative=[${delta.x}, ${delta.z}]")
                }
            }
        }
    }
}
