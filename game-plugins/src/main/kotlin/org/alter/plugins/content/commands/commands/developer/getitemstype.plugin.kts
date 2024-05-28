package org.alter.plugins.content.commands.commands.developer

import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.CacheManager.itemSize
import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

on_command("getitemstype", Privilege.DEV_POWER, description = "Get items type") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::itemsn hat </col>") { values ->
        var items_list = mutableListOf<Int>()
        var item_name = values[0].toInt() // Search trough and if it matches in examine / name add to array and spawn all to bank | $ For spaces

        for (i in 0 until itemSize()) {
            val def = getItem(Item(i).toUnnoted().id)
            val items_name = def.name.lowercase()
            //val items_examine = def.examine?.lowercase()
            if (!def.isPlaceholder && items_name != "null") {
                if (def.equipSlot.equals(item_name)) {
                    items_list.add(def.id)
                }

                }
        }
            for (i in 0 until items_list.count()) {
                player.bank.add(items_list[i], 10)
            }
        player.message("Total Count: ${items_list.count()} with keyword: $item_name in their name and examine")
    }
}