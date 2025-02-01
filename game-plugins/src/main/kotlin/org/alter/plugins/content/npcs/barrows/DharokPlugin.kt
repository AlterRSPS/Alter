package org.alter.plugins.content.npcs.barrows

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

class DharokPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        spawnNpc("npc.dharok_the_wretched", 3576, 3298, 0, 2)
        spawnNpc("npc.dharok_the_wretched", 3576, 3300, 0, 2)
        spawnNpc("npc.dharok_the_wretched", 3573, 3299, 0, 2)
        spawnNpc("npc.dharok_the_wretched", 3578, 3296, 0, 2)
        spawnNpc("npc.dharok_the_wretched", 3574, 3295, 0, 2)

        setCombatDef("npc.dharok_the_wretched") {
            configs {
                attackSpeed = 7
                respawnDelay = 50
            }

            stats {
                hitpoints = 100
                attack = 100
                strength = 100
                defence = 100
            }

            anims {
                attack = 2067
                block = 424
                death = 2925
            }
        }
    }
}
