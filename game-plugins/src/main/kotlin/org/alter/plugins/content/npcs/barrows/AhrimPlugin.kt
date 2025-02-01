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

class AhrimPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        spawnNpc("npc.ahrim_the_blighted", 3565, 3289, 0, 2)
        spawnNpc("npc.ahrim_the_blighted", 3563, 3286, 0, 2)
        spawnNpc("npc.ahrim_the_blighted", 3563, 3291, 0, 2)
        spawnNpc("npc.ahrim_the_blighted", 3567, 3291, 0, 2)
        spawnNpc("npc.ahrim_the_blighted", 3568, 3288, 0, 2)

        setCombatDef("npc.ahrim_the_blighted") {
            configs {
                attackSpeed = 6
                respawnDelay = 50
            }

            stats {
                hitpoints = 100
                magic = 100
                defence = 100
            }

            anims {
                attack = 729
                block = 2079
                death = 2925
            }
        }
    }
}
