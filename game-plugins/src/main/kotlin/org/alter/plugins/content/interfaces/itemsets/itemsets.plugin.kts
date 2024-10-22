import dev.openrune.cache.CacheManager.getEnum
import dev.openrune.cache.CacheManager.getItem
import org.alter.game.model.attr.INTERACTING_ITEM_ID
import org.alter.game.model.attr.INTERACTING_OPT_ATTR
import org.alter.game.model.attr.INTERACTING_SLOT_ATTR
import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.interfaces.itemsets.ItemSets

on_button(ItemSets.ITEMSETS_INTERFACE, 2) {
    val Item = getEnum(Enums.ITEM_SETS).getInt(player.attr[INTERACTING_ITEM_ID]!!)
    if (player.attr[INTERACTING_OPT_ATTR]!! == 1) {
        var missing = 0
        val message: StringBuilder = StringBuilder()
        val itemEnum = getEnum(Item)
        itemEnum.values.filter {it.key != -1 }.forEach { item ->
            if (item.value is Int) {
                val itemDef = getItem(item.value as Int)
                if (!player.inventory.contains(itemDef.id)) {
                    message.append("<col=ef1020>${itemDef.name}</col>")
                    missing += 1
                } else {
                    message.append(itemDef.name)
                }
                if (itemEnum.values.filter {it.key != -1 }[itemEnum.values.size - 1] == item.value ) {
                    message.append(".")
                } else {
                    message.append(", ")
                }
            }
        }
        if (missing == 0) {
            try {
                itemEnum.values.forEach { item ->
                    when (item.key) {
                        -1 -> player.inventory.add(item.value as Int, 1, true)
                        else -> {
                            if (item.value is Int) {
                                player.inventory.remove(item.value as Int, 1, true)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            player.message("This set consist of: ")
            player.message(message.toString())
        }
    } else {
        world.sendExamine(player, player.attr[INTERACTING_ITEM_ID]!!, ExamineEntityType.ITEM)
    }
}

on_button(ItemSets.ITEMSETS_INVENTORY, 0) {
    if (player.getInteractingOption() == 1) {
        val slot = player.attr[INTERACTING_SLOT_ATTR]!!
        val sitem = player.inventory[slot]?.id
        getEnum(Enums.ITEM_SETS).values.filter { it.key == sitem }.firstNotNullOf {
            try {
                val setOfItems = getEnum(it.value as Int)
                if (player.inventory.freeSlotCount >= setOfItems.getSize()) {
                    try {
                        if (sitem != null) {
                            player.inventory.remove(sitem, 1, beginSlot = player.getInteractingSlot())
                            setOfItems.values.filter {it.key != -1 }.forEach {
                                player.inventory.add(it.value as Int, 1)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    player.message("You need ${setOfItems.getSize()} free spaces to unpack this set.")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    } else {
        world.sendExamine(player, player.attr[INTERACTING_ITEM_ID]!!, ExamineEntityType.ITEM)
    }
}

on_command("sets", Privilege.DEV_POWER) {
    ItemSets.open(player)
}
