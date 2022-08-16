package gg.rsmod.plugins.custom

arrayOf(Objs.ALTAR_409, Objs.ALTAR_14860, Objs.CHAOS_ALTAR, Objs.ALTAR_8749, Objs.CHAOS_ALTAR_411, Objs.ALTAR_29941, Objs.ALTAR_20377, Objs.STATUE_39234, Objs.ALTAR_OF_ZAMORAK, Objs.SHRINE, Objs.ALTAR_2640, Objs.ALTAR_28455).forEach { altar ->
    on_obj_option(obj = altar, option = "pray-at") {
        player.queue {
            player.lock()
            player.playSound(2674)
            player.animate(645)
            player.getSkills() .restore(Skills.PRAYER)
            wait(2)
            player.filterableMessage("The Gods have answered your prayers.")
            player.unlock()
        }
    }
}

arrayOf(Objs.BANDOS_ALTAR, Objs.ARMADYL_ALTAR, Objs.SARADOMIN_ALTAR, Objs.ALTAR_42965, Objs.ZAMORAK_ALTAR, Objs.ALTAR_27501, Objs.ALTAR_39723, Objs.ALTAR_28566, Objs.ALTAR_18258).forEach { altar2 ->
    on_obj_option(obj = altar2, option = "pray") {
        player.queue {
            player.lock()
            player.playSound(2674)
            player.animate(645)
            player.getSkills() .restore(Skills.PRAYER)
            wait(2)
            player.filterableMessage("The Gods have answered your prayers")
            player.unlock()
        }
    }
}