package org.alter.plugins.content.interfaces.priceguide

import dev.openrune.cache.CacheManager.getItem
import org.alter.api.ClientScript
import org.alter.api.CommonClientScripts
import org.alter.api.InterfaceDestination
import org.alter.api.ext.*
import org.alter.game.model.ExamineEntityType
import org.alter.game.model.attr.AttributeKey
import org.alter.game.model.container.ContainerStackType
import org.alter.game.model.container.ItemContainer
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.plugins.service.marketvalue.ItemMarketValueService
import java.text.DecimalFormat

/**
 * @author Tom <rspsmods@gmail.com>
 */
object PriceGuide {
    const val PRICE_GUIDE_INTERFACE_ID = 464
    const val PRICE_GUIDE_TAB_INTERFACE_ID = 238

    private val GUIDE_CONTAINER = AttributeKey<ItemContainer>()
    private val TEMP_INV_CONTAINER = AttributeKey<ItemContainer>()

    fun open(p: Player) {
        p.attr[GUIDE_CONTAINER] = ItemContainer(p.inventory.capacity, ContainerStackType.STACK)
        p.attr[TEMP_INV_CONTAINER] = ItemContainer(p.inventory)

        p.setInterfaceUnderlay(color = -1, transparency = -1)
        p.openInterface(interfaceId = PRICE_GUIDE_INTERFACE_ID, dest = InterfaceDestination.MAIN_SCREEN)
        p.openInterface(interfaceId = PRICE_GUIDE_TAB_INTERFACE_ID, dest = InterfaceDestination.TAB_AREA)

        update(p)

        p.setInterfaceEvents(interfaceId = PRICE_GUIDE_INTERFACE_ID, component = 2, range = 0..27, setting = 1086)
        p.runClientScript(
            CommonClientScripts.INTERFACE_INV_INIT,
            15597568,
            93,
            4,
            7,
            0,
            -1,
            "Add<col=ff9040>",
            "Add-5<col=ff9040>",
            "Add-10<col=ff9040>",
            "Add-All<col=ff9040>",
            "Add-X<col=ff9040>",
        )
        p.setInterfaceEvents(interfaceId = PRICE_GUIDE_TAB_INTERFACE_ID, component = 0, range = 0..27, setting = 1086)
    }

    fun close(p: Player) {
        p.closeInterface(PRICE_GUIDE_TAB_INTERFACE_ID)
        p.attr.remove(GUIDE_CONTAINER)
        p.attr.remove(TEMP_INV_CONTAINER)
    }

    fun add(
        p: Player,
        fakeInvSlot: Int,
    ) {
        val container = p.attr[TEMP_INV_CONTAINER] ?: return
        val item = container[fakeInvSlot] ?: return
        add(p, item.id, item.amount)
    }

    fun add(
        p: Player,
        item: Int,
        amount: Int,
    ) {
        val guideContainer = p.attr[GUIDE_CONTAINER] ?: return
        val invContainer = p.attr[TEMP_INV_CONTAINER] ?: return

        if (!getItem(item).isTradeable) {
            p.message("You cannot trade that item.")
            return
        }

        val remove = invContainer.remove(item, amount, assureFullRemoval = false)
        if (remove.completed == 0) {
            return
        }

        val add = guideContainer.add(item, remove.completed, assureFullInsertion = false)

        if (add.completed == 0) {
            p.message("The price guide has no free space left.")
            return
        }

        if (add.getLeftOver() > 0) {
            invContainer.add(item, add.getLeftOver(), assureFullInsertion = false)
        }

        update(p)
    }

    fun depositInventory(p: Player) {
        val guideContainer = p.attr[GUIDE_CONTAINER] ?: return
        val invContainer = p.attr[TEMP_INV_CONTAINER] ?: return

        var any = false
        var anyUntradeables = false

        for (i in 0 until invContainer.capacity) {
            val item = invContainer[i] ?: continue

            if (!getItem(item.id).isTradeable) {
                anyUntradeables = true
                continue
            }

            val remove = invContainer.remove(item.id, item.amount, assureFullRemoval = false)
            if (remove.completed == 0) {
                continue
            }
            val add = guideContainer.add(item.id, remove.completed, assureFullInsertion = false)
            if (add.completed == 0) {
                continue
            }
            if (add.getLeftOver() > 0) {
                invContainer.add(item.id, add.getLeftOver(), assureFullInsertion = false)
            }
            any = true
        }

        when {
            any -> update(p)
            anyUntradeables -> p.message("You have items that cannot be traded.")
            else -> p.message("You have no items that can be checked.")
        }
    }

    fun remove(
        p: Player,
        slot: Int,
        amount: Int,
    ) {
        val guideContainer = p.attr[GUIDE_CONTAINER] ?: return
        val invContainer = p.attr[TEMP_INV_CONTAINER] ?: return

        val item = guideContainer[slot] ?: return

        val remove = guideContainer.remove(item.id, amount, assureFullRemoval = false)
        if (remove.completed == 0) {
            return
        }

        val add = invContainer.add(item.id, remove.completed, assureFullInsertion = false)
        if (add.completed == 0) {
            p.message("You don't have enough free inventory space to do that.")
            return
        }

        if (add.getLeftOver() > 0) {
            guideContainer.add(item.id, add.getLeftOver(), assureFullInsertion = false)
        }

        update(p)
    }

    suspend fun remove(
        p: Player,
        it: QueueTask,
        slot: Int,
        opt: Int,
    ) {
        val container = p.attr[GUIDE_CONTAINER] ?: return
        val item = container[slot] ?: return

        val amount =
            when (opt) {
                1 -> 1
                2 -> 5
                3 -> 10
                4 -> container.getItemCount(item.id)
                5 -> it.inputInt(p)
                10 -> {
                    p.world.sendExamine(p, item.id, ExamineEntityType.ITEM)
                    return
                }
                else -> return
            }
        remove(p = p, slot = slot, amount = amount)
    }

    suspend fun add(
        p: Player,
        it: QueueTask,
        slot: Int,
        opt: Int,
    ) {
        val container = p.attr[TEMP_INV_CONTAINER] ?: return
        val item = container[slot] ?: return

        val amount =
            when (opt) {
                1 -> 1
                2 -> 5
                3 -> 10
                4 -> container.getItemCount(item.id)
                5 -> it.inputInt(p)
                10 -> {
                    p.world.sendExamine(p, item.id, ExamineEntityType.ITEM)
                    return
                }
                else -> return
            }
        add(p = p, item = item.id, amount = amount)
    }

    fun search(
        p: Player,
        item: Int,
    ) {
        val def = getItem(item)
        val valueService = p.world.getService(ItemMarketValueService::class.java)
        val cost = valueService?.get(item) ?: def.cost

        p.setComponentItem(interfaceId = PRICE_GUIDE_INTERFACE_ID, component = 8, item = item, amountOrZoom = 1)
        p.runClientScript(CommonClientScripts.SET_TEXT_ALIGN, 0, 1, 15, 30408716)
        p.setComponentText(
            interfaceId = PRICE_GUIDE_INTERFACE_ID,
            component = 12,
            text = "${def.name}:<br><col=ffffff>${DecimalFormat().format(cost)}</col>",
        )
    }

    fun update(p: Player) {
        val guideContainer = p.attr[GUIDE_CONTAINER] ?: return
        val invContainer = p.attr[TEMP_INV_CONTAINER] ?: return

        p.sendItemContainer(key = 90, container = guideContainer)
        p.sendItemContainer(key = 93, container = invContainer)

        p.setComponentItem(interfaceId = PRICE_GUIDE_INTERFACE_ID, component = 8, item = -1, amountOrZoom = 1)

        val valueService = p.world.getService(ItemMarketValueService::class.java)
        val costs = Array(size = guideContainer.capacity) { 0 }
        guideContainer.forEachIndexed { index, item ->
            if (item != null) {
                val cost = valueService?.get(item.id) ?: getItem(item.id).cost
                costs[index] = cost
            }
        }

        p.runClientScript(ClientScript("ge_pricechecker_prices"), *costs)
        p.runClientScript(CommonClientScripts.SET_TEXT_ALIGN, 1, 1, 15, 30408716)

        // p.setComponentText(interfaceId = PRICE_GUIDE_INTERFACE_ID, component = 12, text = "Total guide price:<br><col=ffffff>${DecimalFormat().format(guideContainer.getNetworth(p.world))}</col>")
    }
}
