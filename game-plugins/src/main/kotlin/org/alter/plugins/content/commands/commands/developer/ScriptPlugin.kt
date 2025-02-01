package org.alter.plugins.content.commands.commands.developer

import org.alter.api.*
import org.alter.api.ClientScript
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

class ScriptPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("script", Privilege.DEV_POWER, description = "Run script by id") {
            val values = player.getCommandArgs()
            val id = values[0].toInt()
            val clientArgs = MutableList<Any>(values.size - 1) {}
            for (arg in 1 until values.size)
                clientArgs[arg - 1] = values[arg].toIntOrNull() ?: values[arg]
            player.runClientScript(ClientScript(id = id), *clientArgs.toTypedArray())
            player.message("Executing <col=0000FF>cs_$id</col><col=801700>$clientArgs</col>")
        }
    }
}
