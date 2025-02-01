package org.alter.plugins.content.interfaces.gameframe.tabs.worn_equipment

import org.alter.api.*
import org.alter.api.CommonClientScripts
import org.alter.api.EquipmentType.Companion.EQUIPMENT_INTERFACE_ID
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.action.EquipAction
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.move.stopMovement
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*
import org.alter.plugins.content.interfaces.equipstats.EquipmentStats.EQUIPMENTSTATS_INTERFACE_ID
import org.alter.plugins.content.interfaces.equipstats.EquipmentStats.EQUIPMENTSTATS_TAB_INTERFACE_ID
import org.alter.plugins.content.interfaces.equipstats.EquipmentStats.sendBonuses

class EquipmentStatsPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        onButton(interfaceId = EQUIPMENTSTATS_TAB_INTERFACE_ID, component = 0) {
            val slot = player.getInteractingSlot()
            val opt = player.getInteractingOption()
            val item = player.inventory[slot] ?: return@onButton

            if (opt == 1) {
                val result = EquipAction.equip(player, item, inventorySlot = slot)
                if (result == EquipAction.Result.SUCCESS) {
                    player.calculateBonuses()
                    sendBonuses(player)
                } else if (result == EquipAction.Result.UNHANDLED) {
                    player.message("You can't equip that.")
                }
            } else if (opt == 10) {
                world.sendExamine(player, item.id, ExamineEntityType.ITEM)
            }
        }

        onButton(interfaceId = EQUIPMENT_INTERFACE_ID, component = 1) {
            if (!player.lock.canInterfaceInteract()) {
                return@onButton
            }
            player.setInterfaceUnderlay(-1, -1)
            player.openInterface(interfaceId = EQUIPMENTSTATS_INTERFACE_ID, dest = InterfaceDestination.MAIN_SCREEN)
            player.openInterface(interfaceId = EQUIPMENTSTATS_TAB_INTERFACE_ID, dest = InterfaceDestination.TAB_AREA)
            player.runClientScript(
                CommonClientScripts.INTERFACE_INV_INIT,
                5570560,
                93,
                4,
                7,
                1,
                -1,
                "Equip",
                "",
                "",
                "",
                ""
            )
            player.setInterfaceEvents(
                interfaceId = EQUIPMENTSTATS_TAB_INTERFACE_ID,
                component = 0,
                range = 0..27,
                setting = 1180674
            )
            player.stopMovement()
            sendBonuses(player)
        }

        onInterfaceClose(interfaceId = EQUIPMENTSTATS_INTERFACE_ID) {
            player.closeInterface(interfaceId = EQUIPMENTSTATS_TAB_INTERFACE_ID)
        }

        bind_unequip(EquipmentType.HEAD, component = 10)
        bind_unequip(EquipmentType.CAPE, component = 11)
        bind_unequip(EquipmentType.AMULET, component = 12)
        bind_unequip(EquipmentType.AMMO, component = 20)
        bind_unequip(EquipmentType.WEAPON, component = 13)
        bind_unequip(EquipmentType.CHEST, component = 14)
        bind_unequip(EquipmentType.SHIELD, component = 15)
        bind_unequip(EquipmentType.LEGS, component = 16)
        bind_unequip(EquipmentType.GLOVES, component = 17)
        bind_unequip(EquipmentType.BOOTS, component = 18)
        bind_unequip(EquipmentType.RING, component = 19)
    }


    fun bind_unequip(
        equipment: EquipmentType,
        component: Int,
    ) {
        onButton(interfaceId = EQUIPMENTSTATS_INTERFACE_ID, component = component) {
            val opt = player.getInteractingOption()

            if (opt == 1) {
                EquipAction.unequip(player, equipment.id)
                player.calculateBonuses()
                sendBonuses(player)
            } else if (opt == 10) {
                val item = player.equipment[equipment.id] ?: return@onButton
                world.sendExamine(player, item.id, ExamineEntityType.ITEM)
            } else {
                val item = player.equipment[equipment.id] ?: return@onButton
                if (!world.plugins.executeItem(player, item.id, opt)) {
                    val slot = player.getInteractingSlot()
                    if (world.devContext.debugButtons) {
                        player.message(
                            "Unhandled button action: [component=[${EQUIPMENTSTATS_INTERFACE_ID}:$component], option=$opt, slot=$slot, item=${item.id}]",
                        )
                    }
                }
            }
        }
    }
}
