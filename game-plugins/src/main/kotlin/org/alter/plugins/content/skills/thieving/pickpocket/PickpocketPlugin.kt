package org.alter.plugins.content.skills.thieving.pickpocket

import dev.openrune.cache.CacheManager.getItem
import org.alter.api.Skills
import org.alter.api.cfg.Animation
import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.entity.GroundItem
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import org.alter.rscm.RSCM.getRSCM

class PickpocketPlugin(
    r: PluginRepository,
    world: World,
    server: Server,
) : KotlinPlugin(r, world, server) {

    init {
        loadService(PickpocketService())

        onWorldInit {
            val service = world.getService(PickpocketService::class.java) ?: return@onWorldInit
            service.entries.forEach { entry ->
                entry.npcs.forEach { npcId ->
                    onNpcOption(npc = npcId, option = "pickpocket") {
                        player.queue { attemptPickpocket(this, player, player.getInteractingNpc(), entry) }
                    }
                }
            }
        }
    }

    private suspend fun attemptPickpocket(task: QueueTask, player: Player, npc: Npc, entry: PickpocketEntry) {
        val npcName = npc.name.ifBlank { "target" }.lowercase()
        val level = player.getSkills().getCurrentLevel(Skills.THIEVING)

        if (level < entry.level) {
            player.message("You need a Thieving level of ${entry.level} to pickpocket this ${npcName}.")
            return
        }

        if (!canReceiveLoot(player, entry)) {
            player.message("You need some inventory space to pickpocket this ${npcName}.")
            return
        }

        player.facePawn(npc)
        player.lock()
        try {
            player.animate(Animation.THIEVING_PICKPOCKET)
            task.wait(2)

            val successChance = computeSuccessChance(level, entry)
            val success = player.world.randomDouble() <= successChance

            if (success) {
                onSuccess(player, npc, entry, npcName)
            } else {
                onFailure(player, npc, entry, npcName)
            }
        } finally {
            player.unlock()
        }
    }

    private fun computeSuccessChance(level: Int, entry: PickpocketEntry): Double {
        val difference = level - entry.level
        val bonus = difference.coerceAtLeast(0) * entry.successBonusPerLevel
        return (entry.baseSuccess + bonus).coerceIn(0.05, 0.95)
    }

    private fun onSuccess(player: Player, npc: Npc, entry: PickpocketEntry, npcName: String) {
        npc.resetFacePawn()

        player.addXp(Skills.THIEVING, entry.experience)

        val loot = rollLoot(entry, player.world)
        val amount = if (loot.min == loot.max) loot.min else player.world.random(loot.min..loot.max)
        val transaction = player.inventory.add(item = loot.item, amount = amount)
        if (transaction.hasFailed()) {
            player.world.spawn(
                GroundItem(
                    item = getRSCM(loot.item),
                    amount = amount,
                    tile = player.tile,
                    owner = player,
                ),
            )
        }

        player.message("You pick the ${npcName}'s pocket.")
    }

    private fun onFailure(player: Player, npc: Npc, entry: PickpocketEntry, npcName: String) {
        player.message("You fail to pick the ${npcName}'s pocket.")
        npc.forceChat("Hands off!")

        if (entry.stun.ticks > 0) {
            npc.facePawn(player)
        } else {
            npc.resetFacePawn()
        }

        val damageRange = entry.stun.damage
        val damage =
            if (damageRange.max == damageRange.min) {
                damageRange.min
            } else {
                player.world.random(damageRange.min..damageRange.max)
            }

        if (entry.stun.ticks > 0) {
            player.stun(entry.stun.ticks)
        }
        if (damage > 0) {
            player.hit(damage)
        }
    }

    private fun rollLoot(entry: PickpocketEntry, world: World): PickpocketLoot {
        if (entry.loot.size == 1) {
            return entry.loot.first()
        }
        val total = entry.loot.sumOf { it.weight }
        val roll = world.randomDouble() * total
        var cumulative = 0.0
        entry.loot.forEach { loot ->
            cumulative += loot.weight
            if (roll < cumulative) {
                return loot
            }
        }
        return entry.loot.last()
    }

    private fun canReceiveLoot(player: Player, entry: PickpocketEntry): Boolean {
        if (!player.inventory.isFull) {
            return true
        }
        return entry.loot.any { loot ->
            val itemId = getRSCM(loot.item)
            val def = getItem(itemId)
            def.stackable && player.inventory.getItemCount(itemId) > 0
        }
    }
}
