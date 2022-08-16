package gg.rsmod.plugins.content.objs.ladder

fun faceWest(player: Player) {
    val direction : Direction = Direction.WEST
    player.faceTile(player.tile.transform(direction.getDeltaX(), direction.getDeltaZ()))
}

fun faceEast(player: Player) {
    val direction : Direction = Direction.EAST
    player.faceTile(player.tile.transform(direction.getDeltaX(), direction.getDeltaZ()))
}

fun moveTo (player: Player, int: Int) {
    val obj = player.getInteractingGameObj()
    player.queue {
        player.walkTo(this, obj.tile)
        wait(3)
        player.animate(int)
        wait(2)
    }
}

fun Player.climbLadder(direction: Direction) {
    queue {
        player.animate(id = 828, delay = 15)
        forceMove(this, ForcedMovement.of(player.tile, player.tile.step(direction, 1), 10, 60, direction.angle))
    }
}


/**Lumby Arches Lower*/
on_obj_option(Objs.LADDER_16683, option = "climb-up") {
    if (player.tile.x == 3229 && player.tile.z == 3223) {
        player.animate(828)
        player.moveTo(3229, 3223, 1)
    } else if (player.tile.x == 3229 && player.tile.z == 3225) {
        player.animate(828)
        player.moveTo(3229, 3223, 1)
    } else if (player.tile.x == 3230 && player.tile.z == 3224) {
        player.animate(828)
        player.moveTo(3229, 3223, 1)
    } else if (player.tile.x == 3228 && player.tile.z == 3224) {
        player.animate(828)
        player.moveTo(3229, 3223, 1)
    } else if (player.tile.x == 3229 && player.tile.z == 3214) {
        player.animate(828)
        player.moveTo(3229, 3214, 1)
    } else if (player.tile.x == 3230 && player.tile.z == 3213) {
        player.animate(828)
        player.moveTo(3229, 3214, 1)
    } else if (player.tile.x == 3228 && player.tile.z == 3213) {
        player.animate(828)
        player.moveTo(3229, 3214, 1)
    } else if (player.tile.x == 3229 && player.tile.z == 3212) {
        player.animate(828)
        player.moveTo(3229, 3214, 1)
    } else if (player.tile.x == 3211 && player.tile.z == 3243) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3211, 3243, 1)
        }
    }
}

/**Lumby Ladder Middle Up*/
on_obj_option(Objs.LADDER_16684, option = "climb-up") {
    if (player.tile.x == 3229 && player.tile.z == 3214) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3214, 2)
        }
    } else if (player.tile.x == 3230 && player.tile.z == 3213) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3214, 2)
        }
    } else if (player.tile.x == 3228 && player.tile.z == 3213) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3214, 2)
        }
    } else if (player.tile.x == 3229 && player.tile.z == 3223) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3223, 2)
        }
    } else if (player.tile.x == 3230 && player.tile.z == 3224) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3223, 2)
        }
    } else if (player.tile.x == 3229 && player.tile.z == 3225) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3223, 2)
        }
    } else if (player.tile.x == 3228 && player.tile.z == 3224) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3223, 2)
        }
    }
}

/**Lumby Ladder Middle Down*/
on_obj_option(Objs.LADDER_16684, option = "climb-down") {
    if (player.tile.x == 3229 && player.tile.z == 3214) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3214, 0)
        }
    } else if (player.tile.x == 3230 && player.tile.z == 3213) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3214, 0)
        }
    } else if (player.tile.x == 3228 && player.tile.z == 3213) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3214, 0)
        }
    } else if (player.tile.x == 3229 && player.tile.z == 3223) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3223, 0)
        }
    } else if (player.tile.x == 3230 && player.tile.z == 3224) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3223, 0)
        }
    } else if (player.tile.x == 3229 && player.tile.z == 3225) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3223, 0)
        }
    } else if (player.tile.x == 3228 && player.tile.z == 3224) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3223, 0)
        }
    }
}

/**Lumby Ladders*/
on_obj_option(Objs.LADDER_16679, option = "climb-down") {
    if (player.tile.x == 3211 && player.tile.z == 3243) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3211, 3243, 0)
        }
    } else if (player.tile.x == 3230 && player.tile.z == 3213) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3214, 1)
        }
    } else if (player.tile.x == 3228 && player.tile.z == 3213) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3214, 1)
        }
    } else if (player.tile.x == 3229 && player.tile.z == 3223) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3223, 1)
        }
    } else if (player.tile.x == 3230 && player.tile.z == 3224) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3223, 1)
        }
    } else if (player.tile.x == 3229 && player.tile.z == 3225) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3223, 1)
        }
    } else if (player.tile.x == 3228 && player.tile.z == 3224) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3223, 1)
        }
    } else if (player.tile.x == 3229 && player.tile.z == 3214) {
        player.queue {
            player.animate(828)
            wait(2)
            player.moveTo(3229, 3214, 1)
        }
    }
}

/**Windmill bottom floor*/
on_obj_option(Objs.LADDER_12964, option = "climb-up") {
    player.animate(828)
    player.moveTo(player.tile.x, player.tile.z, 1)
}

/**Windmill middle floor*/
private val LADDER2 = 12965
on_obj_option(obj = LADDER2, option = "climb") {
    player.queue {
        when (options("Climb Up.", "Climb Down.", title = "Climb up or down the ladder?")) {
            1 -> {
                player.animate(828)
                player.moveTo(player.tile.x, player.tile.z, 2)
            }
            2 -> {
                player.animate(828)
                player.moveTo(player.tile.x, player.tile.z, 0)
            }
        }
    }
}

on_obj_option(obj = LADDER2, option = "climb-up") {
    player.animate(828)
    player.moveTo(player.tile.x, player.tile.z, 2)
}
on_obj_option(obj = LADDER2, option = "climb-down") {
    player.animate(828)
    player.moveTo(player.tile.x, player.tile.z, 0)
}

/**Windmill top floor*/
on_obj_option(Objs.LADDER_12966, option = "climb-down") {
    player.animate(828)
    player.moveTo(player.tile.x, player.tile.z, 1)
}

/**Redwood ladders*/
on_obj_option(obj = Objs.ROPE_LADDER_28857, option = "climb-up") {
    player.queue {
        moveTo(player, 828)
        wait(2)
        if (player.tile.x == 1566 && player.tile.z == 3483 || player.tile.x == 1566 && player.tile.z == 3493) {
            faceEast(player)
            player.moveTo(player.tile.x + 2, player.tile.z, 1)
        } else
            faceWest(player)
        player.moveTo(player.tile.x - 1, player.tile.z, 1)
    }
}

on_obj_option(obj = Objs.ROPE_LADDER_28858, option = "climb-down") {
    val obj = player.getInteractingGameObj()
    player.queue {
        moveTo(player, 828)
        wait(2)
        player.faceTile(obj.tile)
        if (player.tile.x == 1567 && player.tile.z == 3483 || player.tile.x == 1567 && player.tile.z == 3493) {
            player.moveTo(player.tile.x - 1, player.tile.z, 0)
        } else player.moveTo(player.tile.x + 1, player.tile.z, 0)
    }
}

/**Mining guild ladders*/
on_obj_option(Objs.LADDER_30367, option = "climb-down") {
    if (player.getSkills().getCurrentLevel(Skills.MINING) < 60) {
        player.queue {
            when (world.random(1)) {
                0 -> {
                    chatNpc("Sorry, but you're not experienced enough to go in<br>there.", npc = 7713)
                    messageBox("You need a ${Skills.getSkillName(player.world, Skills.MINING)} level of 60 to access the Mining Guild.")
                }
                1 -> {
                    chatNpc("Sorry, but you're not experienced enough to go in<br>there.", npc = 7712)
                    messageBox("You need a ${Skills.getSkillName(player.world, Skills.MINING)} level of 60 to access the Mining Guild.")
                }
            }
        }
    } else
        player.queue {
            wait(2)
            player.animate(828)
            wait(1)
            if (player.tile.x == 3019 && player.tile.z == 3341) {
                player.moveTo(player.tile.x, 9741)
            }
            if (player.tile.x == 3021 && player.tile.z == 3339) {
                player.moveTo(player.tile.x, 9739)
            }
            if (player.tile.x == 3019 && player.tile.z == 3337) {
                player.moveTo(player.tile.x, 9737)
            }
            if (player.tile.x == 3017 && player.tile.z == 3339) {
                player.moveTo(player.tile.x, 9739)
            }
        }
}

/**Dwarven mines*/
on_obj_option(Objs.TRAPDOOR_11867, "climb-down") {
    if (player.tile.x == 3018 && player.tile.z == 3450) {
        player.queue {
            wait(2)
            player.animate(827)
            wait(1)
            player.moveTo(3018, 9850)
        }
    }
    if (player.tile.x == 3020 && player.tile.z == 3450) {
        player.queue {
            wait(2)
            player.animate(827)
            wait(1)
            player.moveTo(3020, 9850)
        }
    }
}
on_obj_option(Objs.LADDER_17387, "climb-up") {
    if (player.tile.x == 3018 && player.tile.z == 9850) {
        player.queue {
            wait(2)
            player.animate(828)
            wait(1)
            player.moveTo(3018, 3450)
        }
    }
}

/**Varrock manhole*/
on_obj_option(Objs.LADDER_11806, option = "climb-up") {
    if (player.tile.x == 3236 && player.tile.z == 9858) {
        player.queue {
            wait(2)
            player.animate(828)
            wait(1)
            player.moveTo(3236, 3458)
        }
    }
}
