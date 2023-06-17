package org.alter.plugins.content.objects;

/**
 *  @author <a href="https://github.com/CloudS3c">Cl0ud</a>
 *  @author <a href="https://www.rune-server.ee/members/376238-cloudsec/">Cl0ud</a>
 *
 */

on_obj_option(obj = Objs.FLAX_14896, option = "pick", lineOfSightDistance = 0) {
    val obj = player.getInteractingGameObj()
    player.queue {
        val route = player.walkTo(this, obj.tile)
        if (route.success) {
            if(player.inventory.isFull) {
                player.message("You don't have room for this flax.")
                return@queue
            } else {
                player.inventory.add(Items.FLAX)
            }
        }
    }

}