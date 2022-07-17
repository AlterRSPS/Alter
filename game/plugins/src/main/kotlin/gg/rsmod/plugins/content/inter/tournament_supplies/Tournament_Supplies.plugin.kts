package gg.rsmod.plugins.content.inter.tournament_supplies

import gg.rsmod.game.model.attr.INTERACTING_ITEM_ID
import gg.rsmod.game.model.attr.INTERACTING_SLOT_ATTR
import gg.rsmod.game.model.priv.Privilege
import java.lang.NullPointerException

on_command("tournament", Privilege.ADMIN_POWER) {
    Tournament_Supplies.open(player)
}

on_button(interfaceId = Tournament_Supplies.TOURNAMENT_SUPPLIES_INTERFACE, component = 4) {
    val itemid = player.attr[INTERACTING_ITEM_ID]!!
    val option = player.getInteractingOption()
    if (world.definitions.getCount(ItemDef::class.java) < itemid) {
        player.message("[Unhandled item] - $itemid")
    } else {
        var amount = when(option) {
            1 -> 1
            2 -> 5
            3 -> 10
            4 -> -1
            else -> return@on_button
        }
        if (amount == -1) {
            player.queue(TaskPriority.WEAK) {
                amount = inputInt("How many would you like to withdraw?")
                if (amount > 0) {
                    if (player.inventory.freeSlotCount < amount && !world.definitions.get(ItemDef::class.java, itemid).stackable) {
                        amount = player.inventory.freeSlotCount
                    }
                    player.inventory.add(itemid, amount)
                }
            }
        } else {
            player.inventory.add(itemid, amount)
        }
    }
}

on_button(interfaceId = Tournament_Supplies.TOURNAMENT_SUPPLIES_INVENTORY_INTERFACE, component = 0) {
    try {
        val slot = player.attr[INTERACTING_SLOT_ATTR]!!
        val amount = when(player.getInteractingOption()) {
            2 -> player.inventory.getItemCount(player.inventory[slot]!!.id)
            else -> 1
        }
        player.inventory.remove(item = player.inventory[slot]!!.id, amount = amount , beginSlot = slot)
    } catch (_: NullPointerException) {
        // @TODO Ye dunno but some times when spamming to fast the click the ['Option'] goes null and console starts bitching
    }
}