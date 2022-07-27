package gg.rsmod.plugins.content.mechanics.inventory

import gg.rsmod.game.action.EquipAction
import gg.rsmod.game.model.attr.*

on_button(InterfaceDestination.INVENTORY.interfaceId/* Inventory interface ID */, 0) {
    val slot: Int? = player.attr[INTERACTING_SLOT_ATTR]
    val option = player.attr[INTERACTING_OPT_ATTR]

    if (slot != null) {
        if (slot < 0 || slot >= player.inventory.capacity) {
            return@on_button
        }
        if (!player.lock.canItemInteract()) {
            return@on_button
        }
        val item = player.inventory[slot] ?: return@on_button
        player.attr[INTERACTING_ITEM_SLOT] = slot

        when(option) {
            7 -> {
                if (world.plugins.canDropItem(player, item.id)) {
                    if (!world.plugins.executeItem(player, item.id, option-1)) {
                            val remove = player.inventory.remove(item, assureFullRemoval = false, beginSlot = slot)
                            if (remove.completed > 0) {
                                val floor = GroundItem(item.id, remove.completed, player.tile, player)
                                remove.firstOrNull()?.let { removed ->
                                    floor.copyAttr(removed.item.attr)
                                }
                                world.spawn(floor)
                            }
                        }
                    }
            }
            3 -> {
                val result = EquipAction.equip(player, item, slot)
                if (result == EquipAction.Result.UNHANDLED && world.devContext.debugItemActions) {
                    player.message("Unhandled item action: [item=${item.id}, slot=${slot}, option=$option]")
                }
            }
            10 -> {
                world.sendExamine(player, item.id, ExamineEntityType.ITEM)
            }
            else -> {
                if (option != null) {
                    if (!world.plugins.executeItem(player, item.id, option-1) && world.devContext.debugItemActions) {
                        player.message("Unhandled item action: [item=${item.id}, slot=${slot}, option=${option-1}]")
                    }
                }
            }
        }
    }
}
/**
 * Logic for swapping items in inventory.
 */
on_component_item_swap(interfaceId = 149, component = 0) {
    val srcSlot = player.attr[INTERACTING_ITEM_SLOT]!!
    val dstSlot = player.attr[OTHER_ITEM_SLOT_ATTR]!!

    val container = player.inventory

    if (srcSlot in 0 until container.capacity && dstSlot in 0 until container.capacity) {
        container.swap(srcSlot, dstSlot)
    } else {
        // Sync the container on the client
        container.dirty = true
    }
}