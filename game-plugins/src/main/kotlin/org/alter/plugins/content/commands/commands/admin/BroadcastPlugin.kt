package org.alter.plugins.content.commands.commands.admin

import org.alter.api.ChatMessageType
import org.alter.api.ext.getCommandArgs
import org.alter.api.ext.message
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.priv.Privilege
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

/**
 * @author Fritz <frikkipafi@gmail.com>
 */
class BroadcastPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("broadcast", Privilege.ADMIN_POWER, description = "Broadcast for everyone") {
            val args = player.getCommandArgs()
            val text = args[0]
            player.world.players.forEach {
                it.message("$text.", ChatMessageType.BROADCAST)
            }
        }
    }
}
