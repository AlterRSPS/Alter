package org.alter.plugins.content.skills.thieving.stall

import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.CacheManager.getObject
import org.alter.api.Skills
import org.alter.api.cfg.Animation
import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.entity.DynamicObject
import org.alter.game.model.entity.GameObject
import org.alter.game.model.entity.GroundItem
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import org.alter.rscm.RSCM.getRSCM

class StallThievingPlugin(
    r: PluginRepository,
    world: World,
    server: Server,
) : KotlinPlugin(r, world, server) {

    init {
        loadService(StallThievingService())

        onWorldInit {
            val service = world.getService(StallThievingService::class.java) ?: return@onWorldInit
            service.entries.forEach { entry ->
                entry.objectIds.forEach { objId ->
                    val stealOptions =
                        getObject(objId).actions.filterNotNull().filter {
                            it.equals("steal-from", ignoreCase = true) || it.equals("steal from", ignoreCase = true)
                        }
                    stealOptions.forEach { option ->
                        onObjOption(obj = objId, option = option) {
                            val obj = player.getInteractingGameObj()
                            player.queue { stealFromStall(this, player, obj, entry) }
                        }
                    }
                }
            }
        }
    }

    private suspend fun stealFromStall(task: QueueTask, player: Player, obj: GameObject, entry: StallEntry) {
        val level = player.getSkills().getCurrentLevel(Skills.THIEVING)
        val stallName = obj.getDef().name?.lowercase()?.let { if (it.startsWith("the ")) it.drop(4) else it } ?: "stall"

        if (!obj.isSpawned(world)) {
            player.message("There's nothing here to steal from right now.")
            return
        }

        if (level < entry.level) {
            player.message("You need a Thieving level of ${entry.level} to steal from this $stallName.")
            return
        }

        if (!canReceiveLoot(player, entry)) {
            player.message("You need some inventory space to steal from this $stallName.")
            return
        }

        player.faceTile(obj.tile)
        player.lock()
        try {
            player.animate(Animation.THIEVING_STALL)
            task.wait(2)

            if (!obj.isSpawned(world)) {
                player.message("There's nothing here to steal from right now.")
                return
            }

            rewardPlayer(player, entry, stallName)
            replaceWithEmpty(player.world, obj, entry)
        } finally {
            player.unlock()
        }
    }

    private fun rewardPlayer(player: Player, entry: StallEntry, stallName: String) {
        player.addXp(Skills.THIEVING, entry.experience)

        val loot = rollLoot(entry, player.world)
        val amount = if (loot.min == loot.max) loot.min else player.world.random(loot.min..loot.max)
        val transaction = player.inventory.add(item = loot.item, amount = amount)

        player.message("You steal from the $stallName.")
    }

    private fun replaceWithEmpty(world: World, obj: GameObject, entry: StallEntry) {
        val tile = obj.tile
        val type = obj.type
        val rot = obj.rot
        val originalId = obj.id
        val emptyId = entry.emptyObjectId

        world.remove(obj)

        val empty = DynamicObject(id = emptyId, type = type, rot = rot, tile = tile)
        world.spawn(empty)

        world.queue {
            wait(entry.respawnTicks)
            if (world.isSpawned(empty)) {
                world.remove(empty)
            }
            world.spawn(DynamicObject(id = originalId, type = type, rot = rot, tile = tile))
        }
    }

    private fun canReceiveLoot(player: Player, entry: StallEntry): Boolean {
        if (!player.inventory.isFull) {
            return true
        }
        return entry.loot.any { loot ->
            val itemId = getRSCM(loot.item)
            val def = getItem(itemId)
            def.stackable && player.inventory.getItemCount(itemId) > 0
        }
    }

    private fun rollLoot(entry: StallEntry, world: World): StallLoot {
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
}
