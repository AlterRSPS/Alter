
import dev.openrune.cache.CacheManager.getEnum
import dev.openrune.cache.CacheManager.getItem
import org.alter.game.model.attr.INTERACTING_OPT_ATTR
import org.alter.game.model.attr.INTERACTING_SLOT_ATTR
import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.interfaces.itemsets.ItemSets

var itemlist= mutableMapOf<Int, MutableList<Int>>()

/**
 * Before you ask why dafuq this shit is here.
 * So when we retrieve the enum list -> the list does not match with the interface. And we need to get correct item id
 * by looking at the array for item by interacting slot.
 * Since as of some rev IF_BTN1 -> Does not send INTERACTING_ITEM_ID.
 */
var slotList = listOf(
        12863, 12865, 12867, 12869, 12871, 12881, 12877, 12873, 12883, 12879,
        12875, 13064, 13066, 12960, 12962, 12972, 12974, 12984, 12986, 12988,
        12990, 13000, 13002, 13012, 13014, 13024, 13026, 21882, 21885, 12964,
        12966, 12968, 12970, 12976, 12978, 12980, 12982, 20376, 20379, 20382,
        20385, 12992, 12994, 12996, 12998, 13004, 13006, 13008, 13010, 13016,
        13018, 13020, 13022, 13028, 13030, 13032, 13034, 13040, 13042, 13044,
        13046, 13048, 13050, 13052, 13054, 13056, 13058, 13060, 13062, 13036,
        13038, 13149, 13151, 13153, 13155, 13157, 13159, 23124, 13163, 13161,
        13165, 13167, 13169, 13171, 13173, 13175, 21049, 21279, 22438, 23110,
        23113, 23116, 23119, 23667, 24333, 24469, 24472, 24475, 25380, 25383,
        25386, 24488, 26554, 26557, 26560, 27355
)

on_world_init {
    val setsEnum = getEnum(1034)
    val set = setsEnum.values
    set.forEach { it ->
        val enumId: Int = it.value as Int
        val keyId: Int = it.key as Int
        val assignItems: MutableList<Int> = mutableListOf()
        getEnum(enumId).values.forEach {
            val param: Int = it.key+1
            val item = it.value as Int
            if (param != 0 && item != 0) {
               assignItems.add(item)
            }
        }
        itemlist[keyId] = assignItems
    }
}



on_button(ItemSets.ITEMSETS_INTERFACE, 2) {
    val Item = slotList[player.attr[INTERACTING_SLOT_ATTR]!!]
    if (player.attr[INTERACTING_OPT_ATTR]!! == 0) {
        var missing: Int = 0
        val message: StringBuilder = StringBuilder()
        if (itemlist[Item] != null) {
            itemlist[Item]?.forEachIndexed { index, item ->
                val itemName = getItem(item).name
                if (!player.inventory.contains(item)) {
                    message.append("<col=ef1020>$itemName</col>")
                    missing += 1
                } else {
                    message.append(itemName)
                }
                if (index == itemlist[Item]!!.size - 1) {
                    message.append(".")
                } else {
                    message.append(", ")
                }
            }
        }
        if (missing == 0) {
            itemlist[Item]?.forEach {
                player.inventory.remove(it)
            }
            player.inventory.add(Item)
        } else {
            player.message("This set consist of: ")
            player.message(message.toString())
        }
    } else {
        world.sendExamine(player, Item, ExamineEntityType.ITEM)
    }
}

on_button(ItemSets.ITEMSETS_INVENTORY, 0) {
    if (player.attr[INTERACTING_OPT_ATTR]!! == 0) {
        val slot = player.attr[INTERACTING_SLOT_ATTR]!!
        val sitem = player.inventory[slot]!!.id
        if (itemlist[sitem] != null) {
            val inventorySlot = player.getInteractingItemId()
            if (player.inventory.freeSlotCount - 1 >= itemlist[sitem]!!.size) {
                if (player.inventory.remove(item = sitem, beginSlot = inventorySlot).hasSucceeded()) {
                    itemlist[sitem]?.forEach {
                        player.inventory.add(it)
                    }
                }
            } else {
                player.message("You need ${itemlist[sitem]!!.size} free space to unpack this set.")
            }
        }
    } else {
        world.sendExamine(player, player.inventory[player.attr[INTERACTING_SLOT_ATTR]!!]!!.id, ExamineEntityType.ITEM)
    }
}
on_command("sets", Privilege.DEV_POWER) {
    ItemSets.open(player)
}
