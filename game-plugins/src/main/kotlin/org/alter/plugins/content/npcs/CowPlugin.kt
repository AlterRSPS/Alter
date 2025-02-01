package org.alter.plugins.content.npcs

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
import org.alter.plugins.content.combat.isBeingAttacked

/**
 *  @author <a href="https://github.com/CloudS3c">Cl0ud</a>
 *  @author <a href="https://www.rune-server.ee/members/376238-cloudsec/">Cl0ud</a>
 *
 */
class CowPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        val cow_npc_list =
            listOf(
                "npc.cow",
            )

        val COW_YELL_DELAY = TimerKey()

        cow_npc_list.forEach { cow ->
            onNpcSpawn(npc = cow) {
                val npc = npc
                npc.timers[COW_YELL_DELAY] = world.random(100..200)
            }
        }

        onTimer(COW_YELL_DELAY) {
            val npc = npc
            if (!npc.isBeingAttacked()) {
                npc.forceChat("Moo")
            }
            npc.timers[COW_YELL_DELAY] = world.random(100..200)
        }

        cow_npc_list.forEach {
            setCombatDef(it) {
                configs {
                    attackSpeed = 6
                    respawnDelay = 45
                    poisonChance = 0.0
                    venomChance = 0.0
                }
                stats {
                    hitpoints = 8
                    attack = 1
                    strength = 1
                    defence = 1
                    magic = 1
                    ranged = 1
                }

                bonuses {
                    defenceStab = -21
                    defenceSlash = -21
                    defenceCrush = -21
                    defenceMagic = -21
                    defenceRanged = -21
                }

                anims {
                    attack = Animation.COW_ATTACK
                    block = Animation.COW_HIT
                    death = Animation.COW_DEATH
                }

                sound {
                    attackSound = Sound.COW_ATTACK
                    blockSound = Sound.COW_HIT
                    deathSound = Sound.COW_DEATH
                }
            }
        }
    }
}
