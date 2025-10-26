package org.alter.plugins.content.commands.commands.all

import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.move.moveTo
import org.alter.game.model.priv.Privilege
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class TeleportsPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        onCommand("home", description = "Teleports you home") {
            val home = world.gameContext.home
            player.moveTo(home)
        }
        onCommand("edge", Privilege.Companion.ADMIN_POWER, description = "Teleports you to Edgeville") {
            player.moveTo(Tile(x = 3087, z = 3499, height = 0))
        }

        onCommand("varrock", Privilege.Companion.ADMIN_POWER, description = "Teleports you to Varrock") {
            player.moveTo(Tile(x = 3211, z = 3424, height = 0))
        }
        onCommand("falador", Privilege.Companion.ADMIN_POWER, description = "Teleports you to Falador") {
            player.moveTo(Tile(x = 2966, z = 3379, height = 0))
        }
        onCommand("lumbridge", Privilege.Companion.ADMIN_POWER, description = "Teleports you to Lumbridge") {
            player.moveTo(Tile(x = 3222, z = 3217, height = 0))
        }
        onCommand("yanille", Privilege.Companion.ADMIN_POWER, description = "Teleports you to Yanille") {
            player.moveTo(Tile(x = 2606, z = 3093, height = 0))
        }
        onCommand("gnome", Privilege.Companion.ADMIN_POWER, description = "Teleports you to Gnome Stronghold") {
            player.moveTo(Tile(x = 2461, z = 3443, height = 0))
        }
        onCommand("thieving", description = "Teleports you to the test thieving") {
            player.moveTo(Tile(x = 2591, z = 4731, height = 0))
        }
    }
}