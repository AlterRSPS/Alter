import gg.rsmod.game.model.attr.INTERACTING_ITEM_ID
import gg.rsmod.plugins.content.inter.itemsets.ItemSets

var itemList= mutableMapOf<Int, MutableList<Int>>()

on_world_init {
    val setsEnum = world.definitions.get(EnumDef::class.java, 1034)
    val set = setsEnum.values
    set.forEach {
        val enumId: Int = it.value as Int
        val keyId: Int = it.key as Int
        val assignItems: MutableList<Int> = mutableListOf()
        world.definitions.get(EnumDef::class.java, enumId).values.forEach {
            val param: Int = it.key+1
            val item = it.value as Int
            if (param != 0 && item != 0) {
               assignItems.add(item)
            }
        }
        itemList[keyId] = assignItems
    }
}



on_button(ItemSets.ITEMSETS_INTERFACE, 2) {
    val Item = player.attr[INTERACTING_ITEM_ID]!!
    var missing: Int = 0
    val message: StringBuilder = StringBuilder()

    if (itemList[Item] != null) {
        itemList[Item]?.forEachIndexed { index, item ->
            val itemName = world.definitions.get(ItemDef::class.java, item).name
            if (!player.inventory.contains(item)) {
                message.append("<col=ef1020>$itemName</col>")
                missing += 1
            } else {
                message.append(itemName)
            }
            if (index == itemList[Item]!!.size - 1) {
                message.append(".")
            } else {
                message.append(", ")
            }
        }
    }
    if (missing == 0) {
        itemList[Item]?.forEach {
            player.inventory.remove(it)
        }
        player.inventory.add(Item)
    } else {
        player.message("This set consist of: ")
        player.message(message.toString())
    }
}

on_button(ItemSets.ITEMSETS_INVENTORY, 0) {
    val Item = player.attr[INTERACTING_ITEM_ID]!!
    if (itemList[Item] != null) {
        val inventorySlot = player.getInteractingItemId()
        if (player.inventory.freeSlotCount - 1 >= itemList[Item]!!.size) {
            if (player.inventory.remove(item = Item, beginSlot = inventorySlot).hasSucceeded()) {
                itemList[Item]?.forEach {
                    player.inventory.add(it)
                }
            }
        } else {
            player.message("You need ${itemList[Item]!!.size} free space to unpack this set.")
        }
    }
}

on_command("sets") {
    ItemSets.open(player)
}
