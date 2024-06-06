package org.alter.plugins.content.skills.thieving.objs

Stall.values().forEach { stall ->
    stall.stalls.forEach { id ->
        if (objHasOption(id, "Steal-from")) {
            on_obj_option(id, "Steal-from") {
                stall.steal(player, id)
            }
        } else if (objHasOption(id, "Steal from")) {
            on_obj_option(id, "Steal from") {
                stall.steal(player, id)
            }
        }
    }
}
