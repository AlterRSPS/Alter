package org.alter.plugins.content.mechanics.multi

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
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*

class MultiwayCombatPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        val MULTIWAY_VARBIT = 4605

        onWorldInit {
            world.getMultiCombatRegions().forEach { region ->
                onEnterRegion(region) {
                    player.setVarbit(MULTIWAY_VARBIT, 1)
                }

                onExitRegion(region) {
                    player.setVarbit(MULTIWAY_VARBIT, 0)
                }
            }

            world.getMultiCombatChunks().forEach { chunk ->
                onEnterChunk(chunk) {
                    player.setVarbit(MULTIWAY_VARBIT, 1)
                }

                onExitChunk(chunk) {
                    player.setVarbit(MULTIWAY_VARBIT, 0)
                }
            }
        }
    }
}
