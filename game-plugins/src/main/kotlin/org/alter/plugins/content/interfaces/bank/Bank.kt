package org.alter.plugins.content.interfaces.bank

import org.alter.api.BonusSlot
import org.alter.api.ClientScript
import org.alter.api.InterfaceDestination
import org.alter.api.ext.*
import org.alter.game.model.World
import org.alter.game.model.container.ItemContainer
import org.alter.game.model.entity.Player
import org.alter.game.model.item.Item
import org.alter.plugins.content.interfaces.bank.BankTabs.SELECTED_TAB_VARBIT
import org.alter.plugins.content.interfaces.bank.BankTabs.getTabsItems
import org.alter.plugins.content.interfaces.equipstats.EquipmentStats
import org.alter.plugins.content.interfaces.equipstats.EquipmentStats.bonusTextMap

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

    fun withdraw(
        p: Player,
        id: Int,
        amt: Int,
        slot: Int,
        placehold: Boolean,
    ) {
        var withdrawn = 0
        val from = p.bank
        val to = p.inventory
        val amount = Math.min(from.getItemCount(id), amt)
        val note = p.getVarbit(WITHDRAW_AS_VARBIT) == 1
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
            val transfer = from.transfer(to, item = copy, fromSlot = i, note = note, unnote = false)
            withdrawn += transfer?.completed ?: 0
            if (from[i] == null) {
                if (placehold || p.getVarbit(ALWAYS_PLACEHOLD_VARBIT) == 1) {
                    val def = item.getDef()
                    /**
                     * Make sure the item has a valid placeholder item in its
                     * definition.
                     */
                    if (def.placeholderLink > 0) {
                        p.bank[i] = Item(def.placeholderLink, -2)
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

    fun deposit(
        player: Player,
        id: Int,
        amt: Int,
    ) {
        println("Deposit method executed ====")
        val from = player.inventory
        val to = player.bank
        val amount = from.getItemCount(id).coerceAtMost(amt)
        var deposited = 0
        for (i in 0 until from.capacity) {
            val item = from[i] ?: continue
            if (item.id != id) {
                continue
            }
            if (deposited >= amount) {
                break
            }
            val curTab = player.getVarbit(SELECTED_TAB_VARBIT)
            val hasEmptySlot = getTabsItems(player, curTab).contains(null)

            val left = amount - deposited
            val copy = Item(item.id, Math.min(left, item.amount))
            if (copy.amount >= item.amount) {
                copy.copyAttr(item)
            }
            /**
             * @TODO Add handling if curTab is not selected --> We load items into main tab. Even if other tabs have empty slots.
             * @TODO When taking tabs first item it will do shift. --> Empty slots are moved not removed.
             */
            var toSlot = to.removePlaceholder(player.world, copy)
            var placeholderOrExistingStack = true
            if (toSlot == -1 && !to.contains(item.id)) {
                placeholderOrExistingStack = false
                //toSlot = to.getLastFreeSlot()
            }
            val transaction = from.transfer(to, item = copy, fromSlot = i, toSlot = toSlot, note = false, unnote = true)
            if (transaction != null) {
                deposited += transaction.completed
            }

            if (deposited > 0) {
                if (curTab != 0 && !placeholderOrExistingStack) {
                    BankTabs.dropToTab(player, curTab, to.getLastFreeSlotReversed() - 1, hasEmptySlot)
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
        p.runClientScript(
            ClientScript(id = 1495),
            "Non-members' capacity: 400<br>Become a member for 400 more.<br>A banker can sell you up to 360 more.<br>+20 for your PIN.<br>Set an Authenticator for 20 more.",
            786439,
            786549,
        )
        sendBonuses(p)
        p.setInterfaceEvents(
            interfaceId = INV_INTERFACE_ID,
            component = 3,
            0..27,
            InterfaceEvent.ClickOp1,
            InterfaceEvent.ClickOp2,
            InterfaceEvent.ClickOp3,
            InterfaceEvent.ClickOp4,
            InterfaceEvent.ClickOp5,
            InterfaceEvent.ClickOp6,
            InterfaceEvent.ClickOp7,
            InterfaceEvent.ClickOp8,
            InterfaceEvent.ClickOp9,
            InterfaceEvent.ClickOp10,
            InterfaceEvent.DRAG_DEPTH1,
            InterfaceEvent.DragTargetable,
        )
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 47, 1..1200, InterfaceEvent.ClickOp1)
        p.setInterfaceEvents(
            interfaceId = INV_INTERFACE_ID,
            component = 19,
            0..27,
            InterfaceEvent.ClickOp1,
            InterfaceEvent.ClickOp2,
            InterfaceEvent.ClickOp3,
            InterfaceEvent.ClickOp4,
            InterfaceEvent.ClickOp10,
        )
        p.setInterfaceEvents(
            interfaceId = BANK_INTERFACE_ID,
            component = 13,
            0..1199,
            InterfaceEvent.ClickOp1,
            InterfaceEvent.ClickOp2,
            InterfaceEvent.ClickOp3,
            InterfaceEvent.ClickOp4,
            InterfaceEvent.ClickOp5,
            InterfaceEvent.ClickOp6,
            InterfaceEvent.ClickOp7,
            InterfaceEvent.ClickOp8,
            InterfaceEvent.ClickOp9,
            InterfaceEvent.ClickOp10,
            InterfaceEvent.DRAG_DEPTH2,
            InterfaceEvent.DragTargetable,
        )
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, 13, 1218..1227, InterfaceEvent.DragTargetable)
        p.setInterfaceEvents(
            interfaceId = BANK_INTERFACE_ID,
            11,
            10..10,
            InterfaceEvent.ClickOp1,
            InterfaceEvent.ClickOp7,
            InterfaceEvent.DragTargetable,
        )
        p.setInterfaceEvents(
            interfaceId = BANK_INTERFACE_ID,
            11,
            11..19,
            InterfaceEvent.ClickOp1,
            InterfaceEvent.ClickOp6,
            InterfaceEvent.ClickOp7,
            InterfaceEvent.DRAG_DEPTH1,
            InterfaceEvent.DragTargetable,
        )
        p.setInterfaceEvents(
            interfaceId = INV_INTERFACE_ID,
            4,
            0..27,
            InterfaceEvent.ClickOp1,
            InterfaceEvent.ClickOp10,
            InterfaceEvent.DRAG_DEPTH1,
            InterfaceEvent.DragTargetable,
        )
        p.setInterfaceEvents(interfaceId = BANK_INTERFACE_ID, component = 50, 0..3, InterfaceEvent.ClickOp1)
        p.setInterfaceEvents(
            interfaceId = INV_INTERFACE_ID,
            component = 13,
            0..27,
            InterfaceEvent.ClickOp1,
            InterfaceEvent.ClickOp2,
            InterfaceEvent.ClickOp3,
            InterfaceEvent.ClickOp4,
            InterfaceEvent.ClickOp10,
        )
        p.setVarbit(BANK_YOUR_LOOT_VARBIT, 0)
    }

    fun sendBonuses(p: Player) {

        with(p) {
            setBankEquipCompText(component = 98, text = bonusTextMap()[0])
            setBankEquipCompText(component = 99, text = bonusTextMap()[1])
            setBankEquipCompText(component = 100, text = bonusTextMap()[2])
            setBankEquipCompText(component = 101, text = bonusTextMap()[3])
            setBankEquipCompText(component = 102, text = bonusTextMap()[4])
            setBankEquipCompText(component = 132, text = bonusTextMap()[5])
            setBankEquipCompText(component = 133, text = bonusTextMap()[6])
            setBankEquipCompText(component = 104, text = bonusTextMap()[7])
            setBankEquipCompText(component = 105, text = bonusTextMap()[8])
            setBankEquipCompText(component = 106, text = bonusTextMap()[9])
            setBankEquipCompText(component = 108, text = bonusTextMap()[10])
            setBankEquipCompText(component = 107, text = bonusTextMap()[11])
            setBankEquipCompText(component = 110, text = bonusTextMap()[12])
            setBankEquipCompText(component = 111, text = bonusTextMap()[13])
            setBankEquipCompText(component = 112, text = bonusTextMap()[14])
            setBankEquipCompText(component = 113, text = bonusTextMap()[15])
            setBankEquipCompText(component = 115, text = bonusTextMap()[16])
            setBankEquipCompText(component = 116, text = bonusTextMap()[17])

        }
        p.runClientScript(
            ClientScript(id = 7065),
            786549,
            786538,
            "Increases your effective accuracy and damage against undead creatures. For multi-target Ranged and Magic attacks, this applies only to the primary target. It does not stack with the Slayer multiplier.",
        )
        p.setComponentText(interfaceId = BANK_INTERFACE_ID, component = 107, text = "Slayer: 0%") // @TODO
    }
    private fun Player.setBankEquipCompText(component: Int, text: String) {
        this.setComponentText(interfaceId = BANK_INTERFACE_ID, component = component, text = text)
    }

    fun ItemContainer.removePlaceholder(
        world: World,
        item: Item,
    ): Int {
        val def = item.toUnnoted().getDef()
        val slot = if (def.placeholderLink > 0) indexOfFirst { it?.id == def.placeholderLink && it.amount == -2 } else -1
        if (slot != -1) {
            this[slot] = null
        }
        return slot
    }

    fun ItemContainer.insert(
        from: Int,
        to: Int,
    ) {
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
