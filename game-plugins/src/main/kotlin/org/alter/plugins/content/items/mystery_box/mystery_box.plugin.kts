package org.alter.plugins.content.items.mystery_box;

import dev.openrune.cache.CacheManager.itemSize
import org.alter.game.model.priv.Privilege

/**
 *  @author <a href="https://github.com/CloudS3c">Cl0ud</a>
 *  @author <a href="https://www.rune-server.ee/members/376238-cloudsec/">Cl0ud</a>
 *
 */

on_item_option(Items.MYSTERY_BOX, "open") {
    val itemLimit = itemSize()
    val item = world.random(0..itemLimit)
    val itemDef = world.definitions.get(ItemDef::class.java, item)
    if (itemDef.name == "" || itemDef.name.lowercase() == "null" || itemDef.isPlaceholder || itemDef.name.isEmpty() || itemDef.noted) {
        return@on_item_option
    }

    if (player.inventory.freeSlotCount > 0) {
        player.inventory.add(item,1, true)
    } else {
        player.bank.add(item, 1000, true)
    }
}

on_command("randbank") {
    repeat(700) {
        val getItemRange = itemSize()
        var getRandId= world.random(0..getItemRange)
        val itemDefs = world.definitions.get(ItemDef::class.java, getRandId)
        if (!itemDefs.isPlaceholder && !itemDefs.noted && itemDefs.name != "" && itemDefs.name.isNotEmpty() && itemDefs.name != "null") {
            player.bank.add(getRandId, 10000)
        }
    }
}
on_command("setamount", Privilege.DEV_POWER, "Set amount of all items in bank") {
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