package org.alter.plugins.content.skills.thieving.chest

import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.CacheManager.getObject
import org.alter.api.Skills
import org.alter.api.cfg.Animation
import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.Tile
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

    private val disarmedTraps = mutableSetOf<Tile>()

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
                        val normalized = option.lowercase()
                        onObjOption(obj = objId, option = option) {
                            val obj = player.getInteractingGameObj()
                            player.queue {
                                when (normalized) {
                                    "search for traps", "search-for-traps" -> searchForTraps(this, player, obj, entry)
                                    else -> lootChest(this, player, obj, entry)
                                }
                            }
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

        if (entry.trap?.enabled == true && !isTrapDisarmed(obj)) {
            triggerTrap(player, obj, entry)
            player.message("You fail to open the $chestName while the trap is active.")
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
            disarmedTraps.remove(obj.tile)
            replaceWithOpen(player.world, obj, entry)
        } finally {
            player.unlock()
        }
    }

    private suspend fun searchForTraps(task: QueueTask, player: Player, obj: GameObject, entry: ChestEntry) {

        val chestName = obj.getDef().name?.lowercase()?.let { if (it.startsWith("the ")) it.drop(4) else it } ?: "chest"

        val level = player.getSkills().getCurrentLevel(Skills.THIEVING)
        if (level < entry.level) {
            player.message("You need a Thieving level of ${entry.level} to search this $chestName for traps.")
            return
        }

        if (!obj.isSpawned(world)) {
            player.message("There's nothing left in this $chestName right now.")
            return
        }

        val trap = entry.trap
        if (trap?.enabled != true) {
            player.message("You find no traps on the $chestName.")
            return
        }

        if (isTrapDisarmed(obj)) {
            player.message("The trap on this $chestName has already been dismantled.")
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

            val success = if (trap.dismantleChance >= 100) {
                true
            } else if (trap.dismantleChance <= 0) {
                false
            } else {
                player.world.random(1..100) <= trap.dismantleChance
            }

            if (success) {
                disarmedTraps.add(obj.tile)
                player.message("You successfully dismantle the trap on the $chestName.")
            } else {
                player.message("You fail to dismantle the trap on the $chestName.")
                triggerTrap(player, obj, entry)
            }
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
        disarmedTraps.remove(tile)

        val open = DynamicObject(id = openId, type = type, rot = rot, tile = tile)
        world.spawn(open)

        world.queue {
            wait(entry.respawnTicks)
            if (world.isSpawned(open)) {
                world.remove(open)
            }
            world.spawn(DynamicObject(id = originalId, type = type, rot = rot, tile = tile))
            disarmedTraps.remove(tile)
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

    private fun isTrapDisarmed(obj: GameObject): Boolean = disarmedTraps.contains(obj.tile)

    private fun triggerTrap(player: Player, obj: GameObject, entry: ChestEntry) {
        val trap = entry.trap ?: return
        if (!trap.enabled) {
            return
        }

        disarmedTraps.remove(obj.tile)

        val damage = when {
            trap.maxDamage <= trap.minDamage -> trap.minDamage
            else -> player.world.random(trap.minDamage..trap.maxDamage)
        }

        if (damage > 0) {
            player.hit(damage)
        }

        player.message("The trap springs and hits you.")
    }
}
