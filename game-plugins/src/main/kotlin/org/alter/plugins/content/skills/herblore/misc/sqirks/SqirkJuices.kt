package org.alter.plugins.content.skills.herblore.misc.sqirks

import dev.openrune.cache.CacheManager.getItem
import org.alter.api.Skills
import org.alter.api.cfg.Items
import org.alter.api.ext.message
import org.alter.game.model.entity.Player
import org.alter.game.model.item.Item

enum class SqirkJuices(val sqirks: Item, val juice: Int, val cookingXpAwarded: Double, val thieveBoost: Int, val runBoostPercent: Double) {
    WINTER(Item(Items.WINTER_SQIRK,5), Items.WINTER_SQIRKJUICE, 12.0, 0, .05),
    SPRING(Item(Items.SPRING_SQIRK, 4), Items.SPRING_SQIRKJUICE, 5.0, 1, .1),
    AUTUMN(Item(Items.AUTUMN_SQIRK, 3), Items.AUTUMN_SQIRKJUICE, 5.0, 2, .15),
    SUMMER(Item(Items.SUMMER_SQIRK, 2), Items.SUMMER_SQIRKJUICE, 5.0, 3, .2);

    fun squirsh(player: Player){
        if(player.inventory.remove(Items.BEER_GLASS).hasSucceeded()){
            if(player.inventory.remove(sqirks).hasSucceeded()){
                player.inventory.add(juice)
                player.addXp(Skills.COOKING, cookingXpAwarded)
            } else {
                player.message("You do not have the required sq'irks needed. You need ${sqirks.amount - player.inventory.getItemCount(sqirks.id)} more ${
                    getItem(
                        sqirks.id
                    ).name}.")
            }
        } else {
            player.message("You need a beer glass to make a sq'irk juice!")
        }
    }
}