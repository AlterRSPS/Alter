package org.alter.plugins.content.skills.slayer

import org.alter.api.Skills
import org.alter.api.ext.message
import org.alter.api.ext.npc
import org.alter.api.ext.pawn
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.attr.INTERACTING_NPC_ATTR
import org.alter.game.model.attr.INTERACTING_PLAYER_ATTR
import org.alter.game.model.entity.Client
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import org.alter.plugins.content.combat.canAttack
import java.lang.ref.WeakReference

class SlayerPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
    init {
        var x = 3250
        SlayerMonster.entries.forEach { monster ->
            spawnNpc(monster.ids[0], x = x, z = 3229)
            x++

            monster.ids.forEach { id ->
                onNpcOption(id, 2) {
                    val target = player.attr[INTERACTING_NPC_ATTR] ?: return@onNpcOption
                    val slayerLvl = player.getSkills()[Skills.SLAYER]

                    if (slayerLvl.currentLevel < monster.level) {
                        player.message("You need a Slayer level of ${monster.level} to attack this monster.")
                        return@onNpcOption
                    }

                    player.attack(target.get()!!)
                }

                onNpcDeath(id) {
                    // TODO: Implement XP reward
                }
            }


        }
    }
}
