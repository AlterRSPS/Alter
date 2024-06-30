package org.alter.plugins.content.interfaces.attack

import org.alter.game.model.attr.NEW_ACCOUNT_ATTR
import org.alter.plugins.content.combat.specialattack.SpecialAttacks
import org.alter.plugins.content.interfaces.attack.AttackTab.ATTACK_STYLE_VARP
import org.alter.plugins.content.interfaces.attack.AttackTab.ATTACK_TAB_INTERFACE_ID
import org.alter.plugins.content.interfaces.attack.AttackTab.DISABLE_AUTO_RETALIATE_VARP
import org.alter.plugins.content.interfaces.attack.AttackTab.SPECIAL_ATTACK_VARP
import org.alter.plugins.content.interfaces.attack.AttackTab.setEnergy

/**
 * First log-in logic (when accounts have just been made).
 */
on_login {
    if (player.attr.getOrDefault(NEW_ACCOUNT_ATTR, false)) {
        setEnergy(player, 100)
    }
    AttackTab.resetRestorationTimer(player)
}

on_timer(AttackTab.SPEC_RESTORE) {
    AttackTab.restoreEnergy(player)
    AttackTab.resetRestorationTimer(player)
}

/**
 * Attack style buttons
 */
on_button(interfaceId = ATTACK_TAB_INTERFACE_ID, component = 4) {
    player.setVarp(ATTACK_STYLE_VARP, 0)
}

on_button(interfaceId = ATTACK_TAB_INTERFACE_ID, component = 8) {
    player.setVarp(ATTACK_STYLE_VARP, 1)
}

on_button(interfaceId = ATTACK_TAB_INTERFACE_ID, component = 12) {
    player.setVarp(ATTACK_STYLE_VARP, 2)
}

on_button(interfaceId = ATTACK_TAB_INTERFACE_ID, component = 16) {
    player.setVarp(ATTACK_STYLE_VARP, 3)
}

/**
 * Toggle auto-retaliate button.
 */
on_button(interfaceId = ATTACK_TAB_INTERFACE_ID, component = 30) {
    player.toggleVarp(DISABLE_AUTO_RETALIATE_VARP)
}

on_button(interfaceId = 160, component = 35) {
    val weaponId = player.equipment[EquipmentType.WEAPON.id]!!.id
    if (SpecialAttacks.executeOnEnable(weaponId)) {
        if (!SpecialAttacks.execute(player, null, world)) {
            player.message("You don't have enough power left.")
        }
    } else {
        player.toggleVarp(SPECIAL_ATTACK_VARP)
    }
}

/**
 * Toggle special attack.
 */
on_button(interfaceId = ATTACK_TAB_INTERFACE_ID, component = 36) {
    val weaponId = player.equipment[EquipmentType.WEAPON.id]!!.id
    if (SpecialAttacks.executeOnEnable(weaponId)) {
        if (!SpecialAttacks.execute(player, null, world)) {
            player.message("You don't have enough power left.")
        }
    } else {
        player.toggleVarp(SPECIAL_ATTACK_VARP)
    }
}

/**
 * Disable special attack when switching weapons.
 */
on_equip_to_slot(EquipmentType.WEAPON.id) {
    player.setVarp(SPECIAL_ATTACK_VARP, 0)
}

/**
 * Disable special attack on log-out.
 */
on_logout {
    player.setVarp(SPECIAL_ATTACK_VARP, 0)
}
