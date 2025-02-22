package org.alter.plugins.content.items.lootingbag

import dev.openrune.cache.CacheManager.getItem
import org.alter.api.*
import org.alter.api.ClientScript
import org.alter.api.CommonClientScripts
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.api.ext.runClientScript
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.attr.GROUNDITEM_PICKUP_TRANSACTION
import org.alter.game.model.attr.INTERACTING_ITEM_SLOT
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*
import org.alter.plugins.service.marketvalue.ItemMarketValueService
import org.alter.rscm.RSCM.getRSCM

class LootingBagPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    val CONTAINER_KEY = ContainerKey("looting_bag", capacity = 28, stackType = ContainerStackType.NORMAL)
    val LOOTING_BAG_CONTAINER_ID = 516
    val INV_CONTAINER_KEY = 93
    val TAB_INTERFACE_ID = 81

    val VALUE_TEXT_COMPONENT = 6

    init {
        registerContainerKey(CONTAINER_KEY) // Mark key as needing to be de-serialized on log-in.

        onLogin {
            /**
             * If a player has a looting bag when they log in, we need to send the item
             * container. If you open a bank before checking/depositing an item
             * in your looting bag, the bag won't have the "view" option on it.
             */
            if (player.inventory.containsAny("item.looting_bag", "item.looting_bag_22586")) {
                val container = player.containers.computeIfAbsent(CONTAINER_KEY) { ItemContainer(CONTAINER_KEY) }
                player.sendItemContainer(LOOTING_BAG_CONTAINER_ID, container)
            }
        }

        onGlobalItemPickup {
            if (player.inventory.contains(getRSCM("item.looting_bag_22586")) && player.inWilderness()) {
                val inventoryTransaction = player.attr[GROUNDITEM_PICKUP_TRANSACTION]?.get() ?: return@onGlobalItemPickup
                val transactionItem = inventoryTransaction.items.first()
                store(player, transactionItem.item, transactionItem.item.amount, transactionItem.slot)
            }
        }

        onItemOption("item.looting_bag", "open") {
            val slott = player.inventory.getItemIndex("item.looting_bag", false)
            open(player, slott)
            player.message("You open your looting bag, ready to fill it.")
        }

        onItemOption("item.looting_bag_22586", "close") {
            val slott = player.inventory.getItemIndex("item.looting_bag_22586", false)
            close(player, slott)
            player.message("You close your looting bag.")
        }

        arrayOf("item.looting_bag", "item.looting_bag_22586").forEach { bag ->
            onItemOption(bag, "check") {
                check(player)
            }

            onItemOption(bag, "deposit") {
                deposit(player)
            }

            onItemOption(bag, "settings") {
                settings(player)
            }

            canDropItem(bag) {
                val slot = player.attr[INTERACTING_ITEM_SLOT] ?: return@canDropItem false
                val itmID = getRSCM(bag)
                player.queue {
                    val container = player.containers[CONTAINER_KEY]
                    val destroy =
                        destroyItem(player,
                            note = if (container != null && container.hasAny) "If you destroy it, the contents will be lost." else "The bag is empty. Are you sure you want to destroy it?",
                            item = itmID,
                            amount = 1,
                        )
                    if (destroy) {
                        player.inventory.remove(item = itmID, amount = 1, beginSlot = slot)
                    }
                }
                return@canDropItem false
            }
        }

        onButton(interfaceId = TAB_INTERFACE_ID, component = 5) {
            val slot = player.getInteractingSlot()
            when (player.getInteractingOption()) {
                1 -> store(player, slot = slot, amount = 1)
                2 -> store(player, slot = slot, amount = 5)
                3 -> store(player, slot = slot, amount = Int.MAX_VALUE)
                4 -> player.queue { store(player, slot = slot, amount = inputInt(player)) }
                5 -> {
                    val container = player.containers.computeIfAbsent(CONTAINER_KEY) { ItemContainer(CONTAINER_KEY) }
                    val item = container[slot] ?: return@onButton
                    world.sendExamine(player, item.id, ExamineEntityType.ITEM)
                }
                9 -> {
                    val item = player.inventory[slot] ?: return@onButton
                    world.sendExamine(player, item.id, ExamineEntityType.ITEM)
                }
            }
        }

        /**
         * "Bank your loot"
         * Bank items from your looting bag.
         */
        onButton(interfaceId = 15, component = 10) {
            val slot = player.getInteractingSlot()
            when (player.getInteractingOption()) {
                1 -> bank(player, slot = slot, amount = 1)
                2 -> bank(player, slot = slot, amount = 5)
                3 -> bank(player, slot = slot, amount = Int.MAX_VALUE)
                4 -> player.queue { bank(player, slot = slot, amount = inputInt(player)) }
                10 -> {
                    val item = player.containers[CONTAINER_KEY]?.get(slot) ?: return@onButton
                    world.sendExamine(player, item.id, ExamineEntityType.ITEM)
                }
            }
        }

        onButton(interfaceId = 15, component = 5) {
            val container = player.containers[CONTAINER_KEY] ?: return@onButton
            when {
                container.isEmpty -> player.message("You have nothing to deposit.")
                bank_all(player, container) -> player.sendItemContainer(LOOTING_BAG_CONTAINER_ID, container)
                else -> player.message("Bank full.")
            }
        }

        onButton(TAB_INTERFACE_ID, component = 2) {
            player.closeInterface(TAB_INTERFACE_ID)
        }

        /**
         * Register "Deposit loot" button from deposit boxes.
         */
        onButton(interfaceId = 192, component = 8) {
            val container = player.containers[CONTAINER_KEY]
            if (container != null && player.inventory.containsAny("item.looting_bag", "item.looting_bag_22586") && bank_all(player, container)) {
                player.sendItemContainer(LOOTING_BAG_CONTAINER_ID, container)
            } else {
                player.message("You have nothing to deposit.")
            }
        }
    }

    fun store(
        p: Player,
        slot: Int,
        amount: Int,
    ) {
        val item = p.inventory[slot] ?: return

        if (item.id == getRSCM("item.looting_bag") || item.id == getRSCM("item.looting_bag_22586")) {
            p.message("You may be surprised to learn that bagception is not permitted.")
            return
        }

        if (!item.toUnnoted().getDef().isTradeable) {
            p.message("Only tradeable items can be put in the bag.")
            return
        }

        if (!p.inWilderness()) {
            p.message("You can't put items in the looting bag unless you're in the Wilderness.")
            return
        }

        store(p, item, amount)
    }

    fun store(
        p: Player,
        item: Item,
        amount: Int,
        beginSlot: Int = -1,
    ): Boolean {
        // val container = p.containers.computeIfAbsent(CONTAINER_KEY) { ItemContainer(world.definitions, CONTAINER_KEY) }

        // val transferred = p.inventory.transfer(container, item = Item(item, amount).copyAttr(item), fromSlot = beginSlot)?.completed ?: 0
        // if (transferred == 0) {
        //   p.message("The bag's too full.")
        return false
        // }
        // p.sendItemContainer(LOOTING_BAG_CONTAINER_ID, container)
        // p.setComponentText(interfaceId = TAB_INTERFACE_ID, component = VALUE_TEXT_COMPONENT, text = "Bag value: ${p.inventory.getNetworth(world).decimalFormat()} coins")
        // return true
    }

    fun bank(
        p: Player,
        slot: Int,
        amount: Int,
    ) {
        val container = p.containers[CONTAINER_KEY] ?: return
        val item = container[slot] ?: return

        val transfer = container.transfer(p.bank, item = Item(item, amount).copyAttr(item), unnote = true)?.completed ?: 0
        if (transfer == 0) {
            p.message("Bank full.")
            return
        }
        p.sendItemContainer(LOOTING_BAG_CONTAINER_ID, container)
    }

    fun bank_all(
        p: Player,
        container: ItemContainer,
    ): Boolean {
        var any = false

        container.forEach { item ->
            if (item != null) {
                val transfer = container.transfer(p.bank, item = item, unnote = true)?.completed ?: 0
                if (transfer != 0) {
                    any = true
                }
            }
        }

        return any
    }

    fun open(
        p: Player,
        slot: Int,
    ) {
        val remove = p.inventory.remove(item = "item.looting_bag", beginSlot = slot)
        if (remove.hasSucceeded()) {
            p.inventory.add(item ="item.looting_bag_22586", beginSlot = slot)
        }
    }

    fun close(
        p: Player,
        slot: Int,
    ) {
        val remove = p.inventory.remove(item = "item.looting_bag_22586", beginSlot = slot)
        if (remove.hasSucceeded()) {
            p.inventory.add(item = "item.looting_bag", beginSlot = slot)
        }
    }

    fun settings(p: Player) {
        p.queue {
            when (options(p, "... ask how many to store.", "... always store as many as possible.", title = "When using items on the bag...")) {
                // TODO: impl effect
                1 -> {
                    p.message("When using items on the bag, you will be asked how many of that item you wish to store in the bag.")
                }
                2 -> {
                    p.message("When using items on the bag, you will immediately store as many of that item as possible in the bag.")
                }
            }
        }
    }

    fun check(p: Player) {
        val container = p.containers.computeIfAbsent(CONTAINER_KEY) { ItemContainer(CONTAINER_KEY) }

        p.runClientScript(CommonClientScripts.INTERFACE_INV_INIT, 81 shl 16 or 5, LOOTING_BAG_CONTAINER_ID, 4, 7, 0, -1, "", "", "", "", "Examine")
        p.openInterface(dest = InterfaceDestination.TAB_AREA, interfaceId = TAB_INTERFACE_ID)
        p.setInterfaceEvents(interfaceId = TAB_INTERFACE_ID, component = 5, range = 0..27, setting = 32)

        p.runClientScript(CommonClientScripts.LOOTING_BAG_SETUP, "Looting bag", 0)
        p.sendItemContainer(LOOTING_BAG_CONTAINER_ID, container)
        /** @TODO */
        // p.setComponentText(interfaceId = TAB_INTERFACE_ID, component = VALUE_TEXT_COMPONENT, text = "Value: ${container.getNetworth(world).decimalFormat()} coins")
        p.runClientScript(ClientScript(id = 1235), LOOTING_BAG_CONTAINER_ID, *get_item_prices(world, container))

        set_queue(p)
    }

    fun deposit(p: Player) {
        p.openInterface(dest = InterfaceDestination.TAB_AREA, interfaceId = TAB_INTERFACE_ID)
        p.setInterfaceEvents(interfaceId = TAB_INTERFACE_ID, component = 5, range = 0..27, setting = 542)
        p.runClientScript(CommonClientScripts.LOOTING_BAG_SETUP, "Add to bag", 1)

        /** @TODO */
        // p.setComponentText(interfaceId = TAB_INTERFACE_ID, component = VALUE_TEXT_COMPONENT, text = "Bag value: ${p.inventory.getNetworth(world).decimalFormat()} coins")
        p.runClientScript(ClientScript(id = 1235), INV_CONTAINER_KEY, *get_item_prices(world, p.inventory))

        set_queue(p)
    }

    fun set_queue(p: Player) {
        p.queue(TaskPriority.STRONG) {
            terminateAction = {
                if (p.interfaces.isVisible(TAB_INTERFACE_ID)) {
                    p.closeInterface(TAB_INTERFACE_ID)
                }
            }
            waitInterfaceClose(TAB_INTERFACE_ID)
        }
    }

    fun get_item_prices(
        world: World,
        container: ItemContainer,
    ): Array<Int> {
        val marketService = world.getService(ItemMarketValueService::class.java)
        val prices = Array(container.capacity) { -1 }

        container.forEachIndexed { index, item ->
            if (item != null) {
                prices[index] = marketService?.get(item.id) ?: getItem(item.id).cost
            }
        }

        return prices
    }

}
