package org.alter.plugins.content.commands.commands.developer

import org.alter.api.ext.getCommandArgs
import org.alter.api.ext.message
import org.alter.api.ext.player
import org.alter.api.ext.setVarbit
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.priv.Privilege
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class SpellbookPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("spellbook", Privilege.DEV_POWER, description = "Switch between spellbooks") {
            val args = player.getCommandArgs()
            val id = args[0].toInt()
            if (id > 3) {
                player.message("SpellBook does not exist.")
            }
            player.setVarbit(4070, id)
        }
    }
}
