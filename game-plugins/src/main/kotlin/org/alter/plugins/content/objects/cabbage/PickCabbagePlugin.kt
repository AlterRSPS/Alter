package org.alter.plugins.content.objects.cabbage

//import org.alter.game.model.move.walkTo
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

class PickCabbagePlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        
    }
    //
//
//val RESPAWN_DELAY = 75
//
//on_obj_option(obj = Objs.CABBAGE_1161, option = "pick", lineOfSightDistance = 0) {
//    val obj = player.getInteractingGameObj()
//
//    player.queue {
//        val route = player.walkTo(obj.tile)
//        if (route.success) {
//            if (player.inventory.isFull) {
//                player.message("You don't have room for this cabbage.")
//                return@queue
//            }
//            if (obj.isSpawned(world)) {
//                val item = if (world.percentChance(5.0)) Items.CABBAGE_SEED else Items.CABBAGE
//                player.animate(827)
//                player.inventory.add(item = item)
//                world.remove(obj)
//                world.queue {
//                    wait(RESPAWN_DELAY)
//                    world.spawn(DynamicObject(obj))
//                }
//            }
//        } else {
//            player.message(Entity.YOU_CANT_REACH_THAT)
//        }
//    }
//}

}
