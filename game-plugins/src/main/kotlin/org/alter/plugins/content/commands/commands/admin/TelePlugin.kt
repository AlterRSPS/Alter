package org.alter.plugins.content.commands.commands.admin

import org.alter.api.ext.getCommandArgs
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.move.moveTo
import org.alter.game.model.priv.Privilege
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class TelePlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("tele", Privilege.ADMIN_POWER, description = "Teleport to coordinates") {
            val values = player.getCommandArgs()
            val x = values[0].toInt()
            val y = values[1].toInt()
            val height = if (values.size > 2) values[2].toInt() else 0
            player.moveTo(x, y, height)
        }
    }
}
