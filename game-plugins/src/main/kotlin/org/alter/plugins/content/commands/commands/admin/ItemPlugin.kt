package org.alter.plugins.content.commands.commands.admin

import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.CacheManager.itemSize
import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.entity.Player
import org.alter.game.model.item.Item
import org.alter.game.model.priv.Privilege
import org.alter.game.model.queue.QueueTask
import org.alter.game.model.queue.TaskPriority
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import java.text.DecimalFormat

class ItemPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {

        onCommand("item", Privilege.ADMIN_POWER, description = "Spawn items") {
            val values = player.getCommandArgs()
            try {
                val item = values[0].toInt()
                val amount = if (values.size > 1) Math.min(Int.MAX_VALUE.toLong(), values[1].parseAmount()).toInt() else 1
                if (item < itemSize()) {
                    val def = getItem(Item(item).toUnnoted().id)
                    val result = player.inventory.add(item = item, amount = amount, assureFullInsertion = false)
                    player.message(
                        "You have spawned <col=801700>${DecimalFormat().format(result.completed)} x ${def.name}</col></col> ($item).",
                    )
                } else {
                    player.message("Item $item does not exist in cache.")
                }
            } catch (e: Exception) {
                player.queue(TaskPriority.STRONG) {
                    val item = spawn(player) ?: return@queue
                    if (item.amount > 0) {
                        player.message("You have spawned ${item.amount} x ${item.getName()}.")
                    } else {
                        player.message("You don't have enough inventory space.")
                    }
                }
            }
        }
    }

    suspend fun QueueTask.spawn(player: Player): Item? {
        val item = searchItemInput(player, "Select an item to spawn:")
        if (item == -1) {
            return null
        }
        val amount =
            when (options(player, "1", "5", "X", "Max", title = "How many would you like to spawn?")) {
                1 -> 1
                2 -> 5
                3 -> inputInt(player, "Enter amount to spawn")
                4 -> Int.MAX_VALUE
                else -> return null
            }
        val add = player.inventory.add(item, amount, assureFullInsertion = false)
        return Item(item, add.completed)
    }
}
