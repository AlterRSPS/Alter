package org.alter.plugins.content.commands.commands.admin

import org.alter.api.ext.getCommandArgs
import org.alter.api.ext.message
import org.alter.api.ext.openShop
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.priv.Privilege
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class OpenShopPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        listOf("openshop", "shop", "store").forEach {
            onCommand(it, Privilege.ADMIN_POWER, description = "Open shop , use: ::listshop for shop id's") {
                val args = player.getCommandArgs()
                try {
                    player.openShop(args[0].toInt())
                } catch (e: NumberFormatException) {
                    player.openShop(args.joinToString().replace(",", ""))
                } catch (e: Exception) {
                    player.message(e.toString())
                }
            }
        }
    }
}
