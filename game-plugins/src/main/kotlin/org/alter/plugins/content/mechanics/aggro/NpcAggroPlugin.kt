package org.alter.plugins.content.mechanics.aggro

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
import org.alter.plugins.content.combat.getCombatTarget
import org.alter.plugins.content.combat.isAttacking

class NpcAggroPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    val AGGRO_CHECK_TIMER = TimerKey()

    init {
        onGlobalNpcSpawn {
            if (npc.combatDef.aggressiveRadius > 0 && npc.combatDef.aggroTargetDelay > 0) {
                npc.aggroCheck = defaultAggressiveness
                npc.timers[AGGRO_CHECK_TIMER] = npc.combatDef.aggroTargetDelay
            }
        }

        onTimer(AGGRO_CHECK_TIMER) {
            if ((!npc.isAttacking() || npc.tile.isMulti(world)) && npc.lock.canAttack() && npc.isActive()) {
                checkRadius(npc)
            }
            npc.timers[AGGRO_CHECK_TIMER] = npc.combatDef.aggroTargetDelay
        }
    }
    


val defaultAggressiveness: (Npc, Player) -> Boolean = boolean@{ n, p ->
    if (n.combatDef.aggressiveTimer == Int.MAX_VALUE) {
        return@boolean true
    } else if (n.combatDef.aggressiveTimer == Int.MIN_VALUE) {
        return@boolean false
    }

    if (Math.abs(world.currentCycle - p.lastMapBuildTime) > n.combatDef.aggressiveTimer) {
        return@boolean false
    }

    val npcLvl = n.def.combatLevel
    return@boolean p.combatLevel <= npcLvl * 2
}

fun checkRadius(npc: Npc) {
    val radius = npc.combatDef.aggressiveRadius

    mainLoop@
    for (x in -radius..radius) {
        for (z in -radius..radius) {
            val tile = npc.tile.transform(x, z)
            val chunk = world.chunks.get(tile, createIfNeeded = false) ?: continue

            val players = chunk.getEntities<Player>(tile, EntityType.PLAYER, EntityType.CLIENT)
            if (players.isEmpty()) {
                continue
            }

            val targets = players.filter { canAttack(npc, it) }
            if (targets.isEmpty()) {
                continue
            }

            val target = targets.random()
            if (npc.getCombatTarget() != target) {
                npc.attack(target)
            }
            break@mainLoop
        }
    }
}

fun canAttack(
    npc: Npc,
    target: Player,
): Boolean {
    if (!target.isOnline || target.invisible) {
        return false
    }
    return npc.aggroCheck == null || npc.aggroCheck?.invoke(npc, target) == true
}

}
