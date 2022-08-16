package gg.rsmod.plugins.content.objs.ladder

/**Climbing up stairs.*/
arrayOf(Objs.STAIRCASE_16671).forEach { stair_up ->
    on_obj_option(obj = stair_up, option = "climb-up")
    {
        climbupstairs(player)
    }
}

/**Interacting with multilevel stairs*/
arrayOf(Objs.STAIRCASE_16672).forEach {staircase ->
    on_obj_option(obj = staircase, option = "climb")
    {
        player.queue {
            when (options("Climb up the stairs.", "Climb down the stairs.")) {
                1 -> climbupstairs(player)
                2 -> climbdownstairs(player)
            }
        }
    }
}

/**Climbing down the stairs.*/
arrayOf(Objs.STAIRCASE_16673).forEach {stair_down ->
    on_obj_option(obj = stair_down, option ="climb-down") {
        climbdownstairs(player)
    }

}
fun climbupstairs(player: Player) {
    player.moveTo(player.tile.x, player.tile.z, player.tile.height + 1)
}

fun climbdownstairs(player: Player) {
    player.moveTo(player.tile.x, player.tile.z, player.tile.height -1)
}
