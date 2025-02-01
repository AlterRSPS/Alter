package org.alter.plugins.content.items.consumables.food

import org.alter.api.*
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
import org.alter.plugins.content.items.consumables.food.Foods
import org.alter.plugins.content.items.food.Food

class EatingPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        Food.values.forEach { food ->
            onItemOption(item = food.item, option = "eat") {
                if (!Foods.canEat(player, food)) {
                    return@onItemOption
                }

                val inventorySlot = player.getInteractingItemId()
                if (player.inventory.remove(item = food.item, beginSlot = inventorySlot).hasSucceeded()) {
                    Foods.eat(player, food)
                    if (food.replacement != -1) {
                        player.inventory.add(item = food.replacement, beginSlot = inventorySlot)
                    }
                }
            }
        }
    }
}
