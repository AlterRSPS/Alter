package org.alter.plugins.content.interfaces.gameframe.tabs.worn_equipment.priceguide

import org.alter.api.*
import org.alter.api.EquipmentType.Companion.EQUIPMENT_INTERFACE_ID
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*
import org.alter.plugins.content.interfaces.priceguide.PriceGuide
import org.alter.plugins.content.interfaces.priceguide.PriceGuide.PRICE_GUIDE_INTERFACE_ID
import org.alter.plugins.content.interfaces.priceguide.PriceGuide.PRICE_GUIDE_TAB_INTERFACE_ID
import org.alter.plugins.content.interfaces.priceguide.PriceGuide.add
import org.alter.plugins.content.interfaces.priceguide.PriceGuide.close
import org.alter.plugins.content.interfaces.priceguide.PriceGuide.depositInventory
import org.alter.plugins.content.interfaces.priceguide.PriceGuide.remove
import org.alter.plugins.content.interfaces.priceguide.PriceGuide.search

class PriceGuidePlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onButton(interfaceId = EQUIPMENT_INTERFACE_ID, component = 3) {
            if (!player.lock.canInterfaceInteract()) {
                return@onButton
            }
            PriceGuide.open(player)
        }

        onInterfaceClose(interfaceId = PRICE_GUIDE_INTERFACE_ID) {
            close(player)
            player.closeInputDialog()
        }

        onButton(interfaceId = PRICE_GUIDE_TAB_INTERFACE_ID, component = 0) {
            player.queue(TaskPriority.WEAK) {
                add(player, this, player.getInteractingSlot(), player.getInteractingOption())
            }
        }

        onButton(interfaceId = PRICE_GUIDE_INTERFACE_ID, component = 2) {
            player.queue(TaskPriority.WEAK) {
                remove(player,this, player.getInteractingSlot(), player.getInteractingOption())
            }
        }

        onButton(interfaceId = PRICE_GUIDE_INTERFACE_ID, component = 10) {
            depositInventory(player)
        }

        onButton(interfaceId = PRICE_GUIDE_INTERFACE_ID, component = 5) {
            player.queue(TaskPriority.WEAK) {
                val item = searchItemInput(player, "Select an item to ask about its price:")
                search(player, item)
            }
        }
    }
}
