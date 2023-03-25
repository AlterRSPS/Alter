package gg.rsmod.plugins.content.mechanics.starter

import gg.rsmod.game.model.attr.NEW_ACCOUNT_ATTR

on_login {
    val newAccount = player.attr[NEW_ACCOUNT_ATTR] ?: return@on_login
    if (newAccount) {
        val inventory = player.getInventoryStarterItems()
        val bank = player.getBankStarterItems()

        inventory.forEach { slotItem ->
            player.inventory.add(item = slotItem.item, beginSlot = slotItem.slot)
        }

        bank.forEach { slotItem ->
            player.bank.add(item = slotItem.item, beginSlot = slotItem.slot)
        }
    }
}

fun Player.getInventoryStarterItems() = getStarterItems(inventory.capacity, { getItem }, { getItemAmount })

fun Player.getBankStarterItems() = getStarterItems(bank.capacity, { getBankItem }, { getBankItemAmount })

fun getStarterItems(containerCapacity: Int, itemProperty: (Int).() -> String, amountProperty: (Int).() -> String): List<SlotItem> {
    val items = mutableListOf<SlotItem>()
    for (i in 0 until containerCapacity) {
        val item = getProperty<Int>(itemProperty(i)) ?: continue
        val amt = getProperty<Int>(amountProperty(i)) ?: 1
        items.add(SlotItem(i, Item(item, amt)))
    }
    return items
}

val Int.getItem: String
    get() = "item[$this]"

val Int.getItemAmount: String
    get() = "amount[$this]"

val Int.getBankItem: String
    get() = "bank_item[$this]"

val Int.getBankItemAmount: String
    get() = "bank_amount[$this]"