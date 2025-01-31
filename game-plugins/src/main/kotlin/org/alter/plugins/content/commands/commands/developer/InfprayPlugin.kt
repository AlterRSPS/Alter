package org.alter.plugins.content.commands.commands.developer

import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.bits.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.priv.Privilege
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*

class InfprayPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("infpray", Privilege.DEV_POWER, description = "Infinite prayer points") {
            player.toggleStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.PRAY)
            player.message(
                "Infinite prayer: ${if (!player.hasStorageBit(
                        INFINITE_VARS_STORAGE,
                        InfiniteVarsType.PRAY,
                    )
                ) {
                    "<col=801700>disabled</col>"
                } else {
                    "<col=178000>enabled</col>"
                }}",
            )
        }
    }
}
