package org.alter.plugins.content.commands.commands.developer

import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.CacheManager.itemSize
import org.alter.game.model.priv.Privilege


onCommand("getitems", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    var items_list = mutableListOf<Int>()
    var item_name =
        args[0].toString() // Search trough and if it matches in examine / name add to array and spawn all to bank | $ For spaces
    for (i in 0 until itemSize()) {
        val def = getItem(Item(i).toUnnoted().id)
        val items_name = def.name.lowercase()
        val items_examine = def.examine?.lowercase()
        if (!def.isPlaceholder && items_name != "null") {
            if (items_name.contains(item_name, ignoreCase = true)) { // <- Why doesnt this one need a null check? D:
                items_list.add(def.id)
            } else if (items_examine != null) {
                if (items_examine.contains(item_name, ignoreCase = true)) {
                    items_list.add(def.id)
                }
            }
        }
    }
    for (i in 0 until items_list.count()) {
        player.bank.add(items_list[i], 10)
    }
    player.message("Total Count: ${items_list.count()} with keyword: $item_name in their name and examine")
}
