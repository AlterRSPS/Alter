package gg.rsmod.plugins.content.interfaces.bank

import gg.rsmod.game.model.World
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.api.BonusSlot
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.interfaces.bank.BankTabs.BANK_TABLIST_ID
import gg.rsmod.plugins.content.interfaces.bank.BankTabs.BANK_TAB_ROOT_VARBIT
import gg.rsmod.plugins.content.interfaces.bank.BankTabs.SELECTED_TAB_VARBIT
import gg.rsmod.plugins.content.interfaces.bank.BankTabs.getCurrentTab
import gg.rsmod.plugins.content.interfaces.bank.BankTabs.getTabByItem
import gg.rsmod.plugins.content.interfaces.equipstats.EquipmentStats

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Bank {
    const val BANK_INTERFACE_ID = 12
    const val BANK_MAINTAB_COMPONENT = 13
    const val INV_INTERFACE_ID = 15
    const val INV_INTERFACE_CHILD = 3

    const val WITHDRAW_AS_VARBIT = 3958
    const val REARRANGE_MODE_VARBIT = 3959
    const val ALWAYS_PLACEHOLD_VARBIT = 3755
    const val LAST_X_INPUT = 3960
    const val QUANTITY_VARBIT = 6590
    const val INCINERATOR_VARBIT = 5102


    /**
     * Visual varbit for the "Bank your loot" tab area interface when storing
     * items from a looting bag into the bank.
     */
    private const val BANK_YOUR_LOOT_VARBIT = 4139

    fun withdraw(p: Player, id: Int, amt: Int, slot: Int, placehold: Boolean) {
        var withdrawn = 0
        val from = p.bank
        val to = p.inventory
        val amount = Math.min(from.getItemCount(id), amt)
        val note = p.getVarbit(WITHDRAW_AS_VARBIT) == 1
        val oldItemArray = BankTabs.buildBankGrid(p)

        for (i in slot until from.capacity) {
            val item = from[i] ?: continue
            if (item.id != id) {
                continue
            }
            if (withdrawn >= amount) {
                break
            }

            val left = amount - withdrawn
            val copy = Item(item.id, Math.min(left, item.amount))
            if (copy.amount >= item.amount) {
                copy.copyAttr(item)
            }
            val transfer = from.transfer(to, item = copy, fromSlot = i, note = note, unnote = !note)

            withdrawn += transfer?.completed ?: 0

            if (from[i] == null) {
                if (placehold || p.getVarbit(ALWAYS_PLACEHOLD_VARBIT) == 1) {
                    val def = item.getDef(p.world.definitions)
                    /**
                     * Make sure the item has a valid placeholder item in its
                     * definition.
                     */
                    if (def.placeholderLink > 0) {
                        p.bank[i] = Item(def.placeholderLink, -2)
                    }
                } else {
                    var itemsTab: Int = -1
                    if (oldItemArray != null) {
                        itemsTab = getTabByItem(p, item.id, oldItemArray)
                    }

                    val tabed = oldItemArray?.filter { it.tabId == itemsTab }
                    if (tabed!![0].item!!.id == item.id && tabed[0].item!!.amount == 0) {
                        val tabVarbit = BANK_TAB_ROOT_VARBIT + itemsTab

                        val remove = from.shiftV2(tabed[0].slot)
                        val setVarsValue = p.getVarbit(tabVarbit)

                        if (itemsTab != 0) {
                            p.setVarbit(tabVarbit, setVarsValue - remove)
                            if (setVarsValue == 0) {
                                BankTabs.shiftTabs(p, itemsTab)
                                p.setVarbit(SELECTED_TAB_VARBIT, 0)
                            }
                        }
                    }



                }
            }
        }
        if (withdrawn == 0) {
            p.message("You don't have enough inventory space.")
        } else if (withdrawn != amount) {
            p.message("You don't have enough inventory space to withdraw that many.")
        }
    }
    fun deposit(player: Player, id: Int, amt: Int) {
        val from = player.inventory
        val to = player.bank
        val amount = from.getItemCount(id).coerceAtMost(amt)

        var deposited = 0

        for (i in 0 until from.capacity) {

            val item = from[i] ?: continue
            if (item.id != id)
                continue

            if (deposited >= amount) {
                break
            }

            val left = amount - deposited

            val copy = Item(item.id, Math.min(left, item.amount))
            if (copy.amount >= item.amount) {
                copy.copyAttr(item)
            }

            var toSlot = to.removePlaceholder(player.world, copy)
            var placeholderOrExistingStack = true
            val curTab = player.getVarbit(SELECTED_TAB_VARBIT)

            if (toSlot == -1 && !to.contains(item.id)) {
                placeholderOrExistingStack = false
                toSlot = to.getLastFreeSlot()
            }

            val transaction = from.transfer(to, item = copy, fromSlot = i, toSlot = toSlot, note = false, unnote = true)

            if (transaction != null) {
                deposited += transaction.completed
            }

            if (deposited > 0) {
                if (curTab != 0 && !placeholderOrExistingStack) {
                    BankTabs.dropToTab(player, curTab, to.getLastFreeSlotReversed() - 1)
                }
            }
        }

        if (deposited == 0) {
            player.message("Bank full.")
        }
    }

    fun open(p: Player) {
        p.setInterfaceUnderlay(-1, -2)
        p.openInterface(BANK_INTERFACE_ID, InterfaceDestination.MAIN_SCREEN)
        p.openInterface(INV_INTERFACE_ID, InterfaceDestination.TAB_AREA)
        p.setVarp(262, -1)
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 9, text = p.bank.capacity.toString())
        p.runClientScript(1495, "Non-members' capacity: 400<br>Become a member for 400 more.<br>A banker can sell you up to 360 more.<br>+20 for your PIN.<br>Set an Authenticator for 20 more.", 786439, 786549)
        sendBonuses(p)
        // ^ This we can make method.
        p.setInterfaceEvents(interfaceId = INV_INTERFACE_ID, component = 3, 0..27,  InterfaceEvent.ClickOp1, InterfaceEvent.ClickOp2, InterfaceEvent.ClickOp3, InterfaceEvent.ClickOp4, InterfaceEvent.ClickOp5, InterfaceEvent.ClickOp6, InterfaceEvent.ClickOp7, InterfaceEvent.ClickOp8, InterfaceEvent.ClickOp9, InterfaceEvent.ClickOp10, InterfaceEvent.DRAG_DEPTH1, InterfaceEvent.DragTargetable)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 47, 1..1200, InterfaceEvent.ClickOp1)
        p.setInterfaceEvents(interfaceId = INV_INTERFACE_ID, component = 19, 0..27, InterfaceEvent.ClickOp1, InterfaceEvent.ClickOp2, InterfaceEvent.ClickOp3, InterfaceEvent.ClickOp4, InterfaceEvent.ClickOp10)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 13, 0..1199, InterfaceEvent.ClickOp1, InterfaceEvent.ClickOp2, InterfaceEvent.ClickOp3, InterfaceEvent.ClickOp4, InterfaceEvent.ClickOp5, InterfaceEvent.ClickOp6, InterfaceEvent.ClickOp7, InterfaceEvent.ClickOp8, InterfaceEvent.ClickOp9, InterfaceEvent.ClickOp10, InterfaceEvent.DRAG_DEPTH2, InterfaceEvent.DragTargetable)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, 13, 1218..1227, InterfaceEvent.DragTargetable)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, 11, 10..10, InterfaceEvent.ClickOp1, InterfaceEvent.ClickOp7, InterfaceEvent.DragTargetable)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, 11, 11..19, InterfaceEvent.ClickOp1, InterfaceEvent.ClickOp6, InterfaceEvent.ClickOp7, InterfaceEvent.DRAG_DEPTH1, InterfaceEvent.DragTargetable)
        p.setInterfaceEvents(interfaceId = INV_INTERFACE_ID, 4, 0..27, InterfaceEvent.ClickOp1, InterfaceEvent.ClickOp10, InterfaceEvent.DRAG_DEPTH1, InterfaceEvent.DragTargetable)
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 50, 0..3, InterfaceEvent.ClickOp1)
        p.setInterfaceEvents(interfaceId = INV_INTERFACE_ID, component = 13, 0..27, InterfaceEvent.ClickOp1, InterfaceEvent.ClickOp2, InterfaceEvent.ClickOp3, InterfaceEvent.ClickOp4, InterfaceEvent.ClickOp10)
        p.setVarbit(BANK_YOUR_LOOT_VARBIT, 0)
    }
    fun sendBonuses(p: Player) {
        /**
         * @TODO Include set bonus desc
         * Since my account is banned rn can't retrieve how it actl looks if they set the desc when you equip one piece of the set or if the desc gets set when you wear full set. :Shrug:
         */
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 89, text = "Stab: ${EquipmentStats.formatBonus(p, BonusSlot.ATTACK_STAB)}")
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 90, text = "Slash: ${EquipmentStats.formatBonus(p, BonusSlot.ATTACK_SLASH)}")
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 91, text = "Crush: ${EquipmentStats.formatBonus(p, BonusSlot.ATTACK_CRUSH)}")
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 92, text = "Magic: ${EquipmentStats.formatBonus(p, BonusSlot.ATTACK_MAGIC)}")
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 93, text = "Range: ${EquipmentStats.formatBonus(p, BonusSlot.ATTACK_RANGED)}")
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 122, text = "Base: 2.4s") // @TODO Normal Weapon Attack speed
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 123, text = "Actual: 2.4s") // @TODO Attack speed with rapid and etc.
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 95, text = "Stab: ${EquipmentStats.formatBonus(p, BonusSlot.DEFENCE_STAB)}")
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 96, text = "Slash: ${EquipmentStats.formatBonus(p, BonusSlot.DEFENCE_SLASH)}")
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 97, text = "Crush: ${EquipmentStats.formatBonus(p, BonusSlot.DEFENCE_CRUSH)}")
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 99, text = "Range: ${EquipmentStats.formatBonus(p, BonusSlot.DEFENCE_RANGED)}")
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 98, text = "Magic: ${EquipmentStats.formatBonus(p, BonusSlot.DEFENCE_MAGIC)}")
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 101, text = "Melee STR: ${EquipmentStats.formatBonus(p.getStrengthBonus())}")
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 102, text = "Ranged STR: ${EquipmentStats.formatBonus(p.getRangedStrengthBonus())}")
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 103, text = "Magic DMG: ${EquipmentStats.formatBonus(p.getMagicDamageBonus()).toDouble()}%")
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 104, text = "Prayer: ${EquipmentStats.formatBonus(p.getPrayerBonus())}")
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 106, text = "Undead: 0%") // @TODO
        p.runClientScript(7065, 786549, 786538, "Increases your effective accuracy and damage against undead creatures. For multi-target Ranged and Magic attacks, this applies only to the primary target. It does not stack with the Slayer multiplier.")
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 107, text = "Slayer: 0%") // @TODO
    }
    fun ItemContainer.removePlaceholder(world: World, item: Item): Int {
        val def = item.toUnnoted(world.definitions).getDef(world.definitions)
        val slot = if (def.placeholderLink > 0) indexOfFirst { it?.id == def.placeholderLink && it.amount == -2 } else -1
        if (slot != -1) {
            this[slot] = null
        }
        return slot
    }

    fun ItemContainer.insert(from: Int, to: Int) {
        val fromItem = this[from]!! // Shouldn't be null

        this[from] = null

        if (from < to) {
            for (i in from until to) {
                this[i] = this[i + 1]
            }
        } else {
            for (i in from downTo to + 1) {
                this[i] = this[i - 1]
            }
        }
        this[to] = fromItem
    }

}