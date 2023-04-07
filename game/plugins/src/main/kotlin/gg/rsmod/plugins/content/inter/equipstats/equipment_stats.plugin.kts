package gg.rsmod.plugins.content.inter.equipstats

import gg.rsmod.plugins.api.EquipmentType.Companion.EQUIPMENT_INTERFACE_ID
import gg.rsmod.plugins.content.inter.equipstats.EquipmentStats.EQUIPMENTSTATS_INTERFACE_ID
import gg.rsmod.plugins.content.inter.equipstats.EquipmentStats.EQUIPMENTSTATS_TAB_INTERFACE_ID
import gg.rsmod.plugins.content.inter.equipstats.EquipmentStats.sendBonuses
import gg.rsmod.game.action.EquipAction
// ENum 3047
/**
    [enum_3047]
    inputtype=int
    outputtype=string
    default=
    val=0,
    val=1,<col=ffffff>Ahrim The Blighted:<br><br>Blighted Aura:<br><br>Magic Attacks have a 25% chance of lowering the enemy's strength level by 5 on a successful hit.<br><br>
    val=2001,<col=ffffff>Ahrim The Blighted:<br><br>Blighted Aura +:<br><br>Magic Attacks have a 25% chance of lowering the enemy's strength level by 5 on a successful hit.<br><br>If the spell is autocast, it gets a 30% damage bonus.<br><br>
    val=2,<col=ffffff>Dharok the Wretched:<br><br>Wretched Strength:<br><br>Attacks do more damage as your hitpoints decrease. For every hitpoint lost from your maximum hitpoints, you deal 1% more damage.<br><br>
    val=2002,<col=ffffff>Dharok the Wretched:<br><br>Wretched Strength +:<br><br>Attacks do more damage as your hitpoints decrease. For every hitpoint lost from your maximum hitpoints, you deal 1% more damage.<br><br>When you take damage, you have a 1/4 chance of reflecting 15% of the damage to your opponent.<br><br>
    val=3,<col=ffffff>Guthan the Infested:<br><br>Infestation:<br><br>Attacks have a 25% chance of replenishing your health equal to the damage you deal.<br><br>
    val=2003,<col=ffffff>Guthan the Infested:<br><br>Infestation +:<br><br>Attacks have a 25% chance of replenishing your health equal to the damage you deal.<br><br>This can boost your hitpoints by up to 10.<br><br>
    val=4,<col=ffffff>Karil the Tainted:<br><br>Tainted Shot:<br><br>Successful range attacks have a 25% chance of lowering the enemy's agility stat by 20%.<br><br>
    val=2004,<col=ffffff>Karil the Tainted:<br><br>Tainted Shot +:<br><br>Successful range attacks have a 25% chance of lowering the enemy's agility stat by 20%.<br><br>A second hitplat is applied in PvP, dealing half the damage of the first.<br><br>
    val=5,<col=ffffff>Torag the Corrupted:<br><br>Corruption:<br><br>Successful melee attacks have a 25% chance of lowering the victim's run energy by 20%.<br><br>
    val=2005,<col=ffffff>Torag the Corrupted:<br><br>Corruption +:<br><br>Successful melee attacks have a 25% chance of lowering the victim's run energy by 20%.<br><br>For every hitpoint lost from your maximum hitpoints, your Defence stat gets an invisible boost.<br><br>
    val=6,<col=ffffff>Verac the Defiled:<br><br>Defiler:<br><br>Attacks have a 25% chance of ignoring any armour, Defence, and protection prayers.<br><br>
    val=2006,<col=ffffff>Verac the Defiled:<br><br>Defiler +:<br><br>Attacks have a 25% chance of ignoring any armour, Defence, and protection prayers.<br><br>Gives a +7 prayer bonus.<br><br>
    val=7,<col=ffffff>Inquisitor's armour:<br><br>Each individual piece of Inquisitor's armour boosts damage and accuracy by 0.5% when using the crush attack style.<br><br>If all pieces of the Inquisitor's armour are worn, this bonus is increased to 2.5%.<br><br>
    val=8,<col=ffffff>Graceful Outfit:<br><br>Each individual piece of the Graceful Outfit increases your run restoration rate.<br><br>Wearing the full Graceful Outfit increases run energy restoration by 30%.<br><br>
    val=9,<col=ffffff>Obsidian armour:<br><br>All obsidian weaponry will be given a 10% boost in melee accuracy and melee strength.<br><br>
    val=10,<col=ffffff>Void Knight Melee:<br><br>When wearing the Void Top, Robe, Gloves and Melee Helmet, you will receive +10% bonus to your melee damage and accuracy.<br><br>
    val=11,<col=ffffff>Void Knight Range:<br><br>When wearing the Void Top, Robe, Gloves and Ranger Helmet, you will receive +10% bonus to your ranged damage and accuracy.<br><br>
    val=12,<col=ffffff>Void Knight Elite Range:<br><br>When wearing the Elite Void Top, Elite Robe, Gloves and Ranger Helmet, you will receive +12.5% bonus to your ranged damage and 10% bonus to accuracy.<br><br>
    val=13,<col=ffffff>Void Knight Mage:<br><br>When wearing the Void Top, Robe, Gloves and Mage Helmet, you will receive +45% bonus to your magic accuracy.<br><br>
    val=14,<col=ffffff>Void Knight Elite Mage:<br><br>When wearing the Elite Void Top, Elite Robe, Gloves and Mage Helmet, you will receive a +45% bonus to your magic accuracy and 2.5% to your magic damage.<br><br>
    val=15,<col=ffffff>Justiciar:<br><br>PvM damage dealt to you is reduced according to your total armour protection in that style.<br><br>
    val=16,<col=ffffff>Angler outfit:<br><br>XP from training Fishing will be increased by 2.5% if the full set is worn. Incomplete sets give smaller bonuses.<br><br>
    val=17,<col=ffffff>Lumberjack outfit:<br><br>XP from training Woodcutting will be increased by 2.5% if the full set is worn. Incomplete sets give smaller bonuses.<br><br>
    val=18,<col=ffffff>Prospector outfit:<br><br>XP from training Mining will be increased by 2.5% if the full set is worn. Incomplete sets give smaller bonuses.<br><br>
    val=19,<col=ffffff>Farmer outfit:<br><br>XP from training Farming will be increased by 2.5% if the full set is worn. Incomplete sets give smaller bonuses.<br><br>
    val=20,<col=ffffff>Pyromancer outfit:<br><br>XP from training Firemaking will be increased by 2.5% if the full set is worn. Incomplete sets give smaller bonuses.<br><br>
    val=21,<col=ffffff>Shayzien armour:<br><br>Reduces the damage of the acid splash attack used by Lizardman Shamans.<br><br>
    val=22,<col=ffffff>Rogue kit:<br><br>The full set gives a 100% chance of double loot from standard pickpocketing. Incomplete sets give smaller chances.<br><br>
    val=23,<col=ffffff>Carpenter's outfit:<br><br>XP from training Construction will be increased by 2.5% if the full set is worn. Incomplete sets give smaller bonuses.<br><br>
    val=24,<col=ffffff>Crystal armour:<br><br>Each individual piece of Crystal Armour increases your accuracy and damage with crystal ranged weaponry.<br><br>Wearing the full set of Crystal Armour increases accuracy by 30% and damage by 15%.<br><br>
    val=25,<col=ffffff>Swampbark armour:<br><br>The full set increases the duration of Bind, Snare and Entangle spells by 3 seconds<br><br>
    val=26,<col=ffffff>Bloodbark armour:<br><br>The full set increases the amount healed by Blood spells by 32.5%.<br><br>
    val=27,<col=ffffff>Zealot's prayer robes:<br><br>The full set grants a 5% chance of saving ensouled heads when reanimating them and saving bones when burying or offering them. This stacks with the Wilderness altar's bone saving effect.<br><br>
    val=28,<col=ffffff>Spirit Angler outfit:<br><br>XP from training Fishing will be increased by 2.5% if the full set is worn. Incomplete sets give smaller bonuses.<br><br>Full set acts as a tether rope when fighting Tempoross.<br><br>
    val=29,<col=ffffff>Virtus Robes:<br><br>9% increased Magic damage with ancient spells.<br><br>Smoke spells reduce healing received by 30%, as long as the target is poisoned.<br><br>Shadow spells also drain Strength, Ranged, Magic and Defence, as well as Attack.<br><br>Blood spells can heal up to 20% over your maximum hitpoints.<br><br>Ice spells gain 10% additional accuracy against frozen targets.
    val=30,<col=ffffff>Raiments of the Eye:<br><br>Runes crafted are increased by 60%.<br><br>This bonus stacks additively with other similar effects.<br><br>
    val=31,<col=ffffff>Smith's Uniform:<br><br>The full set increases the rate you will smith items at an anvil by 1 tick.<br><br>
 */

fun bind_unequip(equipment: EquipmentType, component: Int) {
    on_button(interfaceId = EQUIPMENTSTATS_INTERFACE_ID, component = component) {
        val opt = player.getInteractingOption()

        if (opt == 1) {
            EquipAction.unequip(player, equipment.id)
            player.calculateBonuses()
            sendBonuses(player)
        } else if (opt == 10) {
            val item = player.equipment[equipment.id] ?: return@on_button
            world.sendExamine(player, item.id, ExamineEntityType.ITEM)
        } else {
            val item = player.equipment[equipment.id] ?: return@on_button
            if (!world.plugins.executeItem(player, item.id, opt)) {
                val slot = player.getInteractingSlot()
                if (world.devContext.debugButtons) {
                    player.message("Unhandled button action: [component=[${EQUIPMENTSTATS_INTERFACE_ID}:$component], option=$opt, slot=$slot, item=${item.id}]")
                }
            }
        }
    }
}

on_button(interfaceId = EQUIPMENTSTATS_TAB_INTERFACE_ID, component = 0) {
    val slot = player.getInteractingSlot()
    val opt = player.getInteractingOption()
    val item = player.inventory[slot] ?: return@on_button

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

on_button(interfaceId = EQUIPMENT_INTERFACE_ID, component = 1) {
    if (!player.lock.canInterfaceInteract()) {
        return@on_button
    }

    player.setInterfaceUnderlay(-1, -1)
    player.openInterface(interfaceId = EQUIPMENTSTATS_INTERFACE_ID, dest = InterfaceDestination.MAIN_SCREEN)
    player.openInterface(interfaceId = EQUIPMENTSTATS_TAB_INTERFACE_ID, dest = InterfaceDestination.TAB_AREA)
    player.runClientScript(149, 5570560, 93, 4, 7, 1, -1, "Equip", "", "", "", "")
    player.setInterfaceEvents(interfaceId = EQUIPMENTSTATS_TAB_INTERFACE_ID, component = 0, range = 0..27, setting = 1180674)

    sendBonuses(player)
}

on_interface_close(interfaceId = EQUIPMENTSTATS_INTERFACE_ID) {
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