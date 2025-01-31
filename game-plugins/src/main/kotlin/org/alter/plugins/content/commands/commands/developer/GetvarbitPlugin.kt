package org.alter.plugins.content.commands.commands.developer

import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.attr.CHANGE_LOGGING
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.priv.Privilege
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*

class GetvarbitPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("getvarbit", Privilege.DEV_POWER, description = "Get varbit state") {
            val args = player.getCommandArgs()
            val varbit = args[0].toInt()
            val state = player.getVarbit(varbit)
            player.message("Get varbit (<col=801700>$varbit</col>): <col=801700>$state</col>")
        }

        onCommand(
            "logchanges",
            Privilege.DEV_POWER,
            description = "Will log all varbits/varps when it gets changed you will see in-game messages.",
        ) {
            val varbitLogging = !(player.attr[CHANGE_LOGGING] ?: false)
            player.attr[CHANGE_LOGGING] = varbitLogging
            player.message("Change Logging: ${if (varbitLogging) "<col=178000>Enabled</col>" else "<col=801700>Disabled</col>"}")
        }
    }
}
