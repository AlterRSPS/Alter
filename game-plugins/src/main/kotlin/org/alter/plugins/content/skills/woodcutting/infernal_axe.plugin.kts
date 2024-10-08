package org.alter.plugins.content.skills.woodcutting

import org.alter.api.cfg.Items
import org.alter.api.ext.player
import org.alter.game.model.attr.AttributeKey

val rechargeItem = intArrayOf(Items.SMOULDERING_STONE, Items.DRAGON_AXE)
val infernalAxe = AttributeKey<Int>("Infernal Axe Charges")

on_item_on_item(item1 = Items.DRAGON_AXE, item2 = Items.SMOULDERING_STONE) {
    Woodcutting.createAxe(player)
}

rechargeItem.forEach { item ->
    on_item_on_item(item1 = Items.INFERNAL_AXE_UNCHARGED, item2 = item) {
        player.inventory.remove(Items.INFERNAL_AXE_UNCHARGED)
        player.inventory.remove(item)
        player.inventory.add(Items.INFERNAL_AXE)
        player.attr.put(infernalAxe, 5000)
    }
}

on_item_option(Items.INFERNAL_AXE, "check") {
    Woodcutting.checkCharges(player)
}
on_equipment_option(Items.INFERNAL_AXE, "check") {
    Woodcutting.checkCharges(player)
}
