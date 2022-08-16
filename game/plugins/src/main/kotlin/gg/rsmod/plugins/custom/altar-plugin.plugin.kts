package gg.rsmod.plugins.custom

/**Altars with the "pray-at" option.*/

arrayOf(
    Objs.ALTAR_409,
    Objs.ALTAR_14860,
    Objs.CHAOS_ALTAR,
    Objs.ALTAR_8749,
    Objs.CHAOS_ALTAR_411,
    Objs.ALTAR_29941,
    Objs.ALTAR_20377,
    Objs.STATUE_39234,
    Objs.ALTAR_OF_ZAMORAK,
    Objs.SHRINE,
    Objs.ALTAR_2640,
    Objs.ALTAR_28455,
    Objs.GORILLA_STATUE_4859,
    Objs.ALTAR_OF_GUTHIX,
    Objs.ALTAR_19145
).forEach { altar ->

    on_obj_option(obj = altar, option = "pray-at") {
        pray(player)

    }
}

/**Altars with the "pray" option.*/

arrayOf(
    Objs.BANDOS_ALTAR,
    Objs.ARMADYL_ALTAR,
    Objs.SARADOMIN_ALTAR,
    Objs.ALTAR_42965,
    Objs.ZAMORAK_ALTAR,
    Objs.ALTAR_27501,
    Objs.ALTAR_39723,
    Objs.ALTAR_28566,
    Objs.ALTAR_18258
).forEach { altar2 ->

    on_obj_option(obj = altar2, option = "pray") {
        pray(player)

    }

}

/**Function for praying at an altar.*/

fun pray(player: Player) {
    player.queue {
        player.lock()
        player.playSound(Sounds.ALTAR_PRAY)
        player.animate(Animation.ALTAR_ANIM)
        player.getSkills() .restore(Skills.PRAYER)
        wait(2)
        player.filterableMessage("Your prayer has been restored.")
        player.unlock()
    }
}