package org.alter.plugins.content.items.ancient_wyvern_shield

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

class AncientWyvernShieldPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        
    }
    //on_item_equip(Items.ANCIENT_WYVERN_SHIELD) {
//    player.animate(3996)
//    player.queue {
//        wait(1)
//        player.graphic(1395, 98)
//    }
//}
///**
// When using bottoms components on anvil it will throw out another message
// If using on anvile => "Perhaps some of the magical apparatus on Fossil Island can help join these two items."
// Theres animation of anvil + hammer , and teleportation purple gfx , and some animation in the end.
// https://www.youtube.com/watch?v=gazwIzzYZKM
// ALSO GIVES SOME AMOUNT OF EXP
//@TODO Also need to shorten these nested if's
//*/
//arrayOf(Items.WYVERN_VISAGE, Items.ELEMENTAL_SHIELD).forEach { item ->
//    on_item_on_obj(obj = Objs.STRANGE_MACHINE_30944, item = item) {
//        player.message("$item was used on ${Objs.STRANGE_MACHINE_30944}")
//        if (player.inventory.contains(Items.WYVERN_VISAGE)) {
//            if (player.inventory.contains(Items.ELEMENTAL_SHIELD)) {
//                if (player.getSkills().getCurrentLevel(Skills.SMITHING) >= 66) {
//                    if (player.getSkills().getCurrentLevel(Skills.MAGIC) >= 66) {
//                        if (player.inventory.contains(Items.HAMMER)) {
//                            player.queue {
//                                // player.animation
//                                player.inventory.remove(Items.WYVERN_VISAGE, 1, true)
//                                player.inventory.remove(Items.ELEMENTAL_SHIELD, 1, true)
//                                player.inventory.add(Items.ANCIENT_WYVERN_SHIELD_21634, 1, true)
//                                // Dialogue @TODO
//                            }
//                        } else {
//                            player.message("You need to have a hammer to do that.")
//                        }
//                    } else {
//                        player.message("You need ${Skills.MAGIC} level of at least 66 to do that.")
//                    }
//                } else {
//                    player.message("You need ${Skills.SMITHING} level of at least 66 to do that.")
//                }
//            } else {
//                player.message("You need to have Elemental Shield to combine this item.")
//            }
//        }
//    }
//}

}
