package gg.rsmod.plugins.content.objs.ladder

arrayOf(Objs.LADDER_12964, Objs.LADDER_12965, Objs.LADDER_16683).forEach { ladder_up ->
    on_obj_option(obj = ladder_up, option = "climb-up") {
        climbupladder(player)
    }
}

arrayOf(Objs.LADDER_12965, Objs.LADDER_12966, Objs.LADDER_16679).forEach { ladder_down ->
    on_obj_option(obj = ladder_down, option = "climb-down") {
        climbdownladder(player)
    }
}

arrayOf(Objs.LADDER_12965, Objs.LADDER_16684).forEach { ladder ->
    on_obj_option(obj = ladder, option = "climb") {
        player.queue {
            when (options("Climb Up", "Climb Down")) {
                1 -> climbupladder(player)
                2 -> climbdownladder(player)
            }
        }
    }
}


fun climbupladder(player : Player) {
    player.queue {
        player.animate(828)
        player.lock()
        wait(2)
        player.moveTo(player.tile.x, player.tile.z, player.tile.height + 1)
        player.unlock()
    }
}

fun climbdownladder(player : Player) {
    player.queue {
        player.animate(828)
        player.lock()
        wait(2)
        player.moveTo(player.tile.x, player.tile.z, player.tile.height - 1)
        player.unlock()
    }
}