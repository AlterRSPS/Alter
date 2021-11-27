package gg.rsmod.plugins.content.inter.tournament_supplies

import gg.rsmod.game.model.attr.INTERACTING_ITEM_ID
import gg.rsmod.game.model.priv.Privilege

var TOURNAMENT_SUPPLIES_INTERFACE = 100

on_interface_open(interfaceId = TOURNAMENT_SUPPLIES_INTERFACE) p@ {
    if (player.privilege.powers.contains(Privilege.DEV_POWER)) {
        player.setInterfaceEvents(TOURNAMENT_SUPPLIES_INTERFACE, component = 4, range = 0..443, setting = 1086)
    } else {
        World.logger.warn("${player.username} was able to open Tournament Supplies Interface.")
    }
}
on_button(interfaceId = TOURNAMENT_SUPPLIES_INTERFACE, component = 4) {
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

