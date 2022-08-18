package gg.rsmod.plugins.content.objs.ladder

/**Stairs*/

val stairs = arrayOf(
    Objs.STAIRCASE_16672,
    Objs.STAIRCASE_16673,
    Objs.STAIRCASE_16671
)

stairs.forEach { stairs ->
    if (if_obj_has_option(obj = stairs, option = "climb")) {
        on_obj_option(obj = stairs, option = "climb") {
            climbstairs(player)
        }
    }
    if (if_obj_has_option(obj = stairs, option = "climb-up")) {
        on_obj_option(obj = stairs, option = "climb-up") {
            climbupstairs(player)
        }
    }
    if (if_obj_has_option(obj = stairs, option = "climb-down")) {
        on_obj_option(obj = stairs, option = "climb-down") {
            climbdownstairs(player)
        }
    }
}

/**Ladders*/

val ladders = arrayOf(
    Objs.LADDER_12964,
    Objs.LADDER_12965,
    Objs.LADDER_16683,
    Objs.LADDER_12966,
    Objs.LADDER_16679,
    Objs.LADDER_16684,
)

ladders.forEach { ladder ->
    if (if_obj_has_option(obj = ladder, option = "climb")) {
        on_obj_option(obj = ladder, option = "climb") {
            climbladder(player)
        }
    }
    if (if_obj_has_option(obj = ladder, option = "climb-up")) {
        on_obj_option(obj = ladder, option = "climb-up") {
            climbupladder(player)
        }
    }
    if (if_obj_has_option(obj = ladder, option = "climb-down")) {
        on_obj_option(obj = ladder, option = "climb-down") {
            climbdownladder(player)
        }
    }
}

/**Function for ladders.*/

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

fun climbladder(player: Player) {
    player.queue {
        when (options("Climb up the ladder.", "Climb down the ladder")) {
            1 -> climbupladder(player)
            2 -> climbdownladder(player)
        }
    }
}

/**Function for stairs.*/

fun climbupstairs(player: Player) {
    player.moveTo(player.tile.x, player.tile.z, player.tile.height + 1)
}

fun climbdownstairs(player: Player) {
    player.moveTo(player.tile.x, player.tile.z, player.tile.height -1)
}

fun climbstairs(player: Player) {
    player.queue {
        when (options("Climb up the stairs.", "Climb down the stairs.")) {
            1 -> climbupstairs(player)
            2 -> climbdownstairs(player)
        }
    }
}

/**Trapdoors.*/

on_obj_option(Objs.TRAPDOOR_14880, option = "climb-down") {
    player.moveTo(3210, 9616, 0)
}
on_obj_option(Objs.LADDER_17385, option = "climb-up") {
    player.moveTo(3210, 3216, 0)
}