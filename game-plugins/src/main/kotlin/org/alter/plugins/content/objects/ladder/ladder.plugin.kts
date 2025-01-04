package org.alter.plugins.content.objects.ladder

import org.alter.game.model.move.moveTo

/**Stairs*/

val stairs =
    arrayOf(
        "object.staircase_16672",
        "object.staircase_16673",
        "object.staircase_16671",
    )

stairs.forEach { stairs ->
    if (objHasOption(obj = stairs, option = "climb")) {
        onObjOption(obj = stairs, option = "climb") {
            climbstairs(player)
        }
    }
    if (objHasOption(obj = stairs, option = "climb-up")) {
        onObjOption(obj = stairs, option = "climb-up") {
            climbupstairs(player)
        }
    }
    if (objHasOption(obj = stairs, option = "climb-down")) {
        onObjOption(obj = stairs, option = "climb-down") {
            climbdownstairs(player)
        }
    }
}

/**Ladders*/

val ladders =
    arrayOf(
        "object.ladder_12964",
        "object.ladder_12965",
        "object.ladder_16683",
        "object.ladder_12966",
        "object.ladder_16679",
        "object.ladder_16684",
    )

ladders.forEach { ladder ->
    if (objHasOption(obj = ladder, option = "climb")) {
        onObjOption(obj = ladder, option = "climb") {
            climbladder(player)
        }
    }
    if (objHasOption(obj = ladder, option = "climb-up")) {
        onObjOption(obj = ladder, option = "climb-up") {
            climbupladder(player)
        }
    }
    if (objHasOption(obj = ladder, option = "climb-down")) {
        onObjOption(obj = ladder, option = "climb-down") {
            climbdownladder(player)
        }
    }
}

/**Function for ladders.*/

fun climbupladder(player: Player) {
    player.queue {
        player.animate(828)
        player.lock()
        wait(2)
        player.moveTo(player.tile.x, player.tile.z, player.tile.height + 1)
        player.unlock()
    }
}

fun climbdownladder(player: Player) {
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
    player.moveTo(player.tile.x, player.tile.z, player.tile.height - 1)
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

onObjOption("object.trapdoor_14880", option = "climb-down") {
    player.moveTo(3210, 9616, 0)
}
onObjOption("object.ladder_17385", option = "climb-up") {
    player.moveTo(3210, 3216, 0)
}
