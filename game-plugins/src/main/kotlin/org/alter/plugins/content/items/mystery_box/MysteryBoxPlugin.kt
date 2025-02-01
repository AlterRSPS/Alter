package org.alter.plugins.content.items.mystery_box

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

/**
 *  @author <a href="https://github.com/CloudS3c">Cl0ud</a>
 *  @author <a href="https://www.rune-server.ee/members/376238-cloudsec/">Cl0ud</a>
 *
 */
class MysteryBoxPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onItemOption("item.mystery_box", 2) {
            val itemLimit = itemSize()
            var item = world.random(0..itemLimit)
            var itemDef = getItem(item)
            do {
                item = world.random(0..itemLimit)
                itemDef = getItem(item)
            } while (
                itemDef.name.isEmpty() ||
                itemDef.name.lowercase() == "null" ||
                itemDef.isPlaceholder ||
                itemDef.noted
            )

            if (player.inventory.freeSlotCount > 0) {
                player.inventory.add(item, 1, true)
            } else {
                player.bank.add(item, 1000, true)
            }
        }

        onCommand("randbank") {
            repeat(700) {
                val getItemRange = itemSize()
                var getRandId = world.random(0..getItemRange)
                val itemDefs = getItem(getRandId)
                if (!itemDefs.isPlaceholder && !itemDefs.noted && itemDefs.name != "" && itemDefs.name.isNotEmpty() && itemDefs.name != "null") {
                    player.bank.add(getRandId, 10000)
                }
            }
        }
        onCommand("setamount", Privilege.DEV_POWER, "Set amount of all items in bank") {
            val args = player.getCommandArgs()
            try {
                player.bank.rawItems.forEachIndexed { index, item ->
                    item!!.let {
                        item.amount = args[0].toInt()
                        player.bank[index] = item
                    }
                }
            } catch (e: Exception) {
                player.message(e.toString())
            }
        }
    }
}
