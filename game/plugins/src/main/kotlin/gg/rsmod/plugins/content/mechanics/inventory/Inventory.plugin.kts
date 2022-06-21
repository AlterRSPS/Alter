package gg.rsmod.plugins.content.mechanics.inventory.gg.rsmod.plugins.content.mechanics.inventory

import gg.rsmod.game.action.EquipAction
import gg.rsmod.game.model.attr.*
import gg.rsmod.game.service.game.ItemMetadataService
import java.lang.ref.WeakReference

val interfaceID = 149

// player.setInterfaceEvents(interfaceId = 149, component = 0, range = 0..27, setting = 3407068)
on_button(interfaceID, 0) {
    // player.attr[INTERACTING_OPT_ATTR]
    // player.attr[INTERACTING_ITEM_ID]
    // player.attr[INTERACTING_SLOT_ATTR]

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

        when(option) {
            7 -> {
                /**
                 * @TODO
                 * Need to add a check, if it's a "Drop" Option , otherwise such as "Dismantle" Option will drop the item aswell.
                 * It already has this [canDropItem] method but it's not for that and you will be writing way too many same code.
                 */
                if (world.plugins.canDropItem(player, item.id)) {
                    val remove = player.inventory.remove(item, assureFullRemoval = false, beginSlot = slot)
                    if (remove.completed > 0) {
                        val floor = GroundItem(item.id, remove.completed, player.tile, player)
                        remove.firstOrNull()?.let { removed ->
                            floor.copyAttr(removed.item.attr)
                        }
                        world.spawn(floor)
                    }
                }
                println(7);
            }
            3 -> {
                val result = EquipAction.equip(player, item, slot)
                if (result == EquipAction.Result.UNHANDLED && world.devContext.debugItemActions) {
                    val itemMetaDataService = player.world.getService(ItemMetadataService::class.java)
                    player.message("Unhandled item action: [item=${item.id}, slot=${slot}, option=$option]")
                }
                println(3);
            }
            10 -> {
                println(10);
                world.sendExamine(player, item.id, ExamineEntityType.ITEM)
            }
            else -> {
                if (option != null) {
                    if (!world.plugins.executeItem(player, item.id, option-1) && world.devContext.debugItemActions) {
                        player.message("Unhandled item action: [item=${item.id}, slot=${slot}, option=$option]")
                    }
                }
            }
        }
    }
}
