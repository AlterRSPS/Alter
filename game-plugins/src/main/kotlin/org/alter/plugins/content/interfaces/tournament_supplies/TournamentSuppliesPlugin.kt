package org.alter.plugins.content.interfaces.tournament_supplies

import dev.openrune.cache.CacheManager.getItem
import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.attr.INTERACTING_ITEM_ID
import org.alter.game.model.attr.INTERACTING_SLOT_ATTR
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.priv.Privilege
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*

class TournamentSuppliesPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("tournament", Privilege.ADMIN_POWER) {
            Tournament_Supplies.open(player)
        }

        onButton(interfaceId = Tournament_Supplies.TOURNAMENT_SUPPLIES_INTERFACE, component = 4) {
            val itemid = player.attr[INTERACTING_ITEM_ID]!!
            val option = player.getInteractingOption()
            if (option == 9) {
                world.sendExamine(player, itemid, type = ExamineEntityType.ITEM)
                return@onButton
            }
            var amount =
                when (option) {
                    1 -> 1
                    2 -> 5
                    3 -> 10
                    4 -> -1
                    else -> return@onButton
                }
            if (amount == -1) {
                player.queue(TaskPriority.WEAK) {
                    amount = inputInt(player, "How many would you like to withdraw?")
                    if (amount > 0) {
                        if (player.inventory.freeSlotCount < amount && !getItem(itemid).stackable
                        ) {
                            amount = player.inventory.freeSlotCount
                        }
                        player.inventory.add(itemid, amount)
                    }
                }
            } else {
                if (getItem(itemid).stackable) {
                    amount = 10000
                }
                player.inventory.add(itemid, amount)
            }
        }

        onButton(interfaceId = Tournament_Supplies.TOURNAMENT_SUPPLIES_INVENTORY_INTERFACE, component = 0) {
            try {
                val opt = player.getInteractingOption()
                val slot = player.attr[INTERACTING_SLOT_ATTR]!!
                if (opt == 9) {
                    world.sendExamine(player, player.inventory[slot]!!.id, type = ExamineEntityType.ITEM)
                    return@onButton
                }
                var amount =
                    when (opt) {
                        1 -> player.inventory.getItemCount(player.inventory[slot]!!.id)
                        else -> 1
                    }
                if (getItem(player.inventory[slot]!!.id).stackable) {
                    amount = player.inventory.getItemCount(player.inventory[slot]!!.id)
                }
                player.inventory.remove(item = player.inventory[slot]!!.id, amount = amount, beginSlot = slot)
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }
}
