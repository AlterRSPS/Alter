package org.alter.plugins.content.skills.thieving.chest

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

class ChestThievingPlugin(
    r: PluginRepository,
    world: World,
    server: Server,
) : KotlinPlugin(r, world, server) {

    private val supportedOptions = setOf(
        "open",
        "open chest",
        "search",
        "search chest",
        "search for traps",
        "search-for-traps",
        "pick lock",
        "pick-lock",
    )

    init {
        loadService(ChestThievingService())

        onWorldInit {
            val service = world.getService(ChestThievingService::class.java) ?: return@onWorldInit
            service.entries.forEach { entry ->
                entry.objectIds.forEach { objId ->
                    val options = getObject(objId).actions.filterNotNull().filter { option ->
                        supportedOptions.contains(option.lowercase())
                    }
                    options.forEach { option ->
                        onObjOption(obj = objId, option = option) {
                            val obj = player.getInteractingGameObj()
                            player.queue { lootChest(this, player, obj, entry) }
                        }
                    }
                }
            }
        }
    }

    private suspend fun lootChest(task: QueueTask, player: Player, obj: GameObject, entry: ChestEntry) {
        val level = player.getSkills().getCurrentLevel(Skills.THIEVING)
        val chestName = obj.getDef().name?.lowercase()?.let { if (it.startsWith("the ")) it.drop(4) else it } ?: "chest"

        if (!obj.isSpawned(world)) {
            player.message("There's nothing left in this $chestName right now.")
            return
        }

        if (level < entry.level) {
            player.message("You need a Thieving level of ${entry.level} to loot this $chestName.")
            return
        }

        if (!canReceiveLoot(player, entry)) {
            player.message("You need some inventory space to loot this $chestName.")
            return
        }

        player.faceTile(obj.tile)
        player.lock()
        try {
            player.animate(Animation.THIEVING_STALL)
            task.wait(2)

            if (!obj.isSpawned(world)) {
                player.message("There's nothing left in this $chestName right now.")
                return
            }

            rewardPlayer(player, entry)
            replaceWithOpen(player.world, obj, entry)
        } finally {
            player.unlock()
        }
    }

    private fun rewardPlayer(player: Player, entry: ChestEntry) {
        player.addXp(Skills.THIEVING, entry.experience)

        entry.loot.forEach { loot ->
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
        }

        player.message("You loot the chest.")
    }

    private fun replaceWithOpen(world: World, obj: GameObject, entry: ChestEntry) {
        val tile = obj.tile
        val type = obj.type
        val rot = obj.rot
        val originalId = obj.id
        val openId = entry.openObjectId

        world.remove(obj)

        val open = DynamicObject(id = openId, type = type, rot = rot, tile = tile)
        world.spawn(open)

        world.queue {
            wait(entry.respawnTicks)
            if (world.isSpawned(open)) {
                world.remove(open)
            }
            world.spawn(DynamicObject(id = originalId, type = type, rot = rot, tile = tile))
        }
    }

    private fun canReceiveLoot(player: Player, entry: ChestEntry): Boolean {
        if (!player.inventory.isFull) {
            return true
        }

        var freeSlots = player.inventory.freeSlotCount
        entry.loot.forEach { loot ->
            val itemId = getRSCM(loot.item)
            val def = getItem(itemId)
            val hasExistingStack = def.stackable && player.inventory.getItemCount(itemId) > 0
            if (!hasExistingStack) {
                if (freeSlots <= 0) {
                    return false
                }
                freeSlots--
            }
        }
        return true
    }
}
