package org.alter.plugins.content.items.mystery_box

import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.CacheManager.itemSize
import org.alter.game.model.priv.Privilege

/**
 *  @author <a href="https://github.com/CloudS3c">Cl0ud</a>
 *  @author <a href="https://www.rune-server.ee/members/376238-cloudsec/">Cl0ud</a>
 *
 */

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
