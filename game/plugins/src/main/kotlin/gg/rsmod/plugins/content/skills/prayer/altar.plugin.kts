package gg.rsmod.plugins.content.skills.prayer

val altars = arrayOf(
    Objs.BANDOS_ALTAR,
    Objs.ARMADYL_ALTAR,
    Objs.SARADOMIN_ALTAR,
    Objs.ALTAR_42965,
    Objs.ZAMORAK_ALTAR,
    Objs.ALTAR_27501,
    Objs.ALTAR_39723,
    Objs.ALTAR_28566,
    Objs.ALTAR_18258,
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
)

altars.forEach { obj ->
    arrayOf("Pray", "Pray-at").forEach {
        if (if_obj_has_option(obj, it)) {
            on_obj_option(obj, it) {
                pray(player)
            }
        }
    }
}

/**
 * Function for praying at an altar.
 */
fun pray(player: Player) {
    if (player.getSkills().getCurrentLevel(Skills.PRAYER) >= player.getSkills().getBaseLevel(Skills.PRAYER)) {
        player.filterableMessage("You already have full Prayer points.")
    } else {
        player.queue {
            player.lock()
            player.getSkills() .restore(Skills.PRAYER)
            player.playSound(Sounds.ALTAR_PRAY)
            player.animate(Animation.PRAY_AT_ALTAR_ANIM)
            wait(2)
            player.filterableMessage("Your prayer has been restored.")
            player.unlock()
        }
    }
}