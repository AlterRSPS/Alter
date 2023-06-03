package org.alter.plugins.content.interfaces.priceguide

import org.alter.api.EquipmentType.Companion.EQUIPMENT_INTERFACE_ID
import org.alter.plugins.content.interfaces.priceguide.PriceGuide.PRICE_GUIDE_INTERFACE_ID
import org.alter.plugins.content.interfaces.priceguide.PriceGuide.PRICE_GUIDE_TAB_INTERFACE_ID
import org.alter.plugins.content.interfaces.priceguide.PriceGuide.add
import org.alter.plugins.content.interfaces.priceguide.PriceGuide.close
import org.alter.plugins.content.interfaces.priceguide.PriceGuide.depositInventory
import org.alter.plugins.content.interfaces.priceguide.PriceGuide.remove
import org.alter.plugins.content.interfaces.priceguide.PriceGuide.search

on_button(interfaceId = EQUIPMENT_INTERFACE_ID, component = 3) {
    if (!player.lock.canInterfaceInteract()) {
        return@on_button
    }
    PriceGuide.open(player)
}

on_interface_close(interfaceId = PRICE_GUIDE_INTERFACE_ID) {
    close(player)
    player.closeInputDialog()
}

on_button(interfaceId = PRICE_GUIDE_TAB_INTERFACE_ID, component = 0) {
    player.queue(TaskPriority.WEAK) {
        add(this, player.getInteractingSlot(), player.getInteractingOption())
    }
}

on_button(interfaceId = PRICE_GUIDE_INTERFACE_ID, component = 2) {
    player.queue(TaskPriority.WEAK) {
        remove(this, player.getInteractingSlot(), player.getInteractingOption())
    }
}

on_button(interfaceId = PRICE_GUIDE_INTERFACE_ID, component = 10) {
    depositInventory(player)
}

on_button(interfaceId = PRICE_GUIDE_INTERFACE_ID, component = 5) {
    player.queue(TaskPriority.WEAK) {
        val item = searchItemInput("Select an item to ask about its price:")
        search(player, item)
    }
}
