package org.alter.plugins.content.commands.commands.developer

import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.priv.Privilege
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*

class OpenurlPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("openurl", Privilege.DEV_POWER, description = "Open url") {
            val args = player.getCommandArgs()
            val url = args[0]
            if (!url.startsWith("http://") || !url.startsWith("https://")) {
                player.openUrl("https://$url") // not perfect by any means, but simple enough as fallback for easier command
            } else {
                player.openUrl(url)
            }
        }
    }
}
