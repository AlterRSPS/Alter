package gg.rsmod.plugins.content.skills.thieving.objs

Stall.values().forEach{ stall ->
    stall.stalls.forEach { id ->
        if (if_obj_has_option(id, "Steal-from")) {
            on_obj_option(id, "Steal-from") {
                stall.steal(player, id)
            }
        } else if (if_obj_has_option(id, "Steal from")) {
            on_obj_option(id, "Steal from") {
                stall.steal(player, id)
            }
        }
    }
}