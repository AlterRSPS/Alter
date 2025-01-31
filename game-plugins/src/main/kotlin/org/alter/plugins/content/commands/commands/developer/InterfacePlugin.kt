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

class InterfacePlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("interface", Privilege.DEV_POWER, description = "Open interface by id") {
            val values = player.getCommandArgs()
            val component = values[0].toInt()
            player.openInterface(component, InterfaceDestination.MAIN_SCREEN)
            player.message("Opening interface <col=801700>$component</col>")
        }
    }
}
