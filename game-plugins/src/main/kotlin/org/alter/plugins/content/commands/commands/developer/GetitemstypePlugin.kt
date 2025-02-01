package org.alter.plugins.content.commands.commands.developer

import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.CacheManager.itemSize
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
import org.alter.game.model.priv.Privilege
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*

class GetitemstypePlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("getitemstype", Privilege.DEV_POWER, description = "Get items type") {
            val args = player.getCommandArgs()
            var items_list = mutableListOf<Int>()
            var item_name =
                args[0].toInt() // Search trough and if it matches in examine / name add to array and spawn all to bank | $ For spaces

            for (i in 0 until itemSize()) {
                val def = getItem(Item(i).toUnnoted().id)
                val items_name = def.name.lowercase()
                // val items_examine = def.examine?.lowercase()
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
}
