package gg.rsmod.plugins.content.objs.ladder

/**Lumbridge kitchen trapdoor*/

on_obj_option(Objs.TRAPDOOR_14880, option = "climb-down") {
    player.moveTo(3210, 9616, 0)
}

on_obj_option(Objs.LADDER_17385, option = "climb-up") {
    player.queue {
        player.animate(828)
        wait(2)
        player.moveTo(3210, 3216, 0)
    }

}