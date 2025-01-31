package org.alter.plugins.content.commands.commands.developer

import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.priv.Privilege
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class VarbitPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("varbit", Privilege.DEV_POWER, description = "Set varbit to amount") {
            val args = player.getCommandArgs()
            val varbit = args[0].toInt()
            val state = args[1].toInt()
            val oldState = player.getVarbit(varbit)
            player.setVarbit(varbit, state)
            player.message(
                "Set varbit (<col=801700>$varbit</col>) from <col=801700>$oldState</col> to <col=801700>${
                    player.getVarbit(
                        varbit
                    )
                }</col>",
            )
        }
    }
}
