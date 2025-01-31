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

class InfrunPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("infrun", Privilege.DEV_POWER, description = "Infinite run energy") {
            player.toggleStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.RUN)
            player.message(
                "Infinite run: ${if (!player.hasStorageBit(
                        INFINITE_VARS_STORAGE,
                        InfiniteVarsType.RUN,
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
