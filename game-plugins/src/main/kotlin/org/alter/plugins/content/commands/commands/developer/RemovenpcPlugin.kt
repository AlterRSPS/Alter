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

class RemovenpcPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("removenpc", Privilege.DEV_POWER, description = "Remove Npc under player") {
            val chunk = world.chunks.getOrCreate(player.tile)
            val npc = chunk.getEntities<Npc>(player.tile, EntityType.NPC).firstOrNull()
            if (npc != null) {
                world.remove(npc)
            } else {
                player.message("No NPC found in tile.")
            }
        }
    }
}
