package org.alter.plugins.content.mechanics.equipment

import org.alter.api.EquipmentType.Companion.EQUIPMENT_INTERFACE_ID
import org.alter.game.action.EquipAction
import org.alter.game.fs.ObjectExamineHolder

fun bind_unequip(
    equipment: EquipmentType,
    child: Int,
) {
    onButton(interfaceId = EQUIPMENT_INTERFACE_ID, component = child) {
        val opt = player.getInteractingOption()
        when (opt) {
            1 -> {
                val result = EquipAction.unequip(player, equipment.id)
                if (equipment == EquipmentType.WEAPON && result == EquipAction.Result.SUCCESS) {
                    player.sendWeaponComponentInformation()
                }
            }
            10 -> {
                val item = player.equipment[equipment.id] ?: return@onButton
                world.sendExamine(player, item.id, ExamineEntityType.ITEM)
            }
            else -> {
                val item = player.equipment[equipment.id] ?: return@onButton
                val menuOpt = opt
                if (!world.plugins.executeEquipmentOption(player, item.id, menuOpt) && world.devContext.debugItemActions) {
                    val action = ObjectExamineHolder.EQUIPMENT_MENU.get(item.id).equipmentMenu[menuOpt]
                    player.message("Unhandled equipment action: [item=${item.id}, option=$menuOpt, action=$action]")
                }
            }
        }
    }
}

for (equipment in EquipmentType.values) {
    onEquipToSlot(equipment.id) {
        if (equipment == EquipmentType.WEAPON) {
            player.sendWeaponComponentInformation()
        }
    }
}

bind_unequip(EquipmentType.HEAD, child = 15)
bind_unequip(EquipmentType.CAPE, child = 16)
bind_unequip(EquipmentType.AMULET, child = 17)
bind_unequip(EquipmentType.WEAPON, child = 18)
bind_unequip(EquipmentType.CHEST, child = 19)
bind_unequip(EquipmentType.SHIELD, child = 20)
bind_unequip(EquipmentType.LEGS, child = 21)
bind_unequip(EquipmentType.GLOVES, child = 22)
bind_unequip(EquipmentType.BOOTS, child = 23)
bind_unequip(EquipmentType.RING, child = 24)
bind_unequip(EquipmentType.AMMO, child = 25)
