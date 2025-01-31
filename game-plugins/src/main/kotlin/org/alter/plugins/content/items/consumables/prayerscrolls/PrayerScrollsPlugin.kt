package org.alter.plugins.content.items.consumables.prayerscrolls

import org.alter.rscm.RSCM.getRSCM
import org.alter.plugins.content.mechanics.prayer.Prayers

onItemOption("item.dexterous_prayer_scroll", "read") {
    player.queue {
        if (player.getVarbit(Prayers.RIGOUR_UNLOCK_VARBIT) == 1) {
            messageBox(
                "You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods. However there's nothing more for you to learn.",
            )
            return@queue
        }
        player.animate(id = 7403)
        itemMessageBox(
            "You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods! Would you like to absorb its power? <br>(Warning: This will consume the scroll.)</b>",
            item = "item.dexterous_prayer_scroll",
        )
        when (options("Learn Rigour", "Cancel", title = "This will consume the scroll")) {
            1 -> {
                if (player.inventory.contains(getRSCM("item.dexterous_prayer_scroll"))) {
                    player.inventory.remove(item = "item.dexterous_prayer_scroll")
                    player.setVarbit(id = Prayers.RIGOUR_UNLOCK_VARBIT, value = 1)
                    player.animate(id = -1)
                    itemMessageBox(
                        "You study the scroll and learn a new prayer: <col=8B0000>Rigour</col>",
                        item = "item.dexterous_prayer_scroll",
                    )
                }
            }
            2 -> {
                player.animate(id = -1)
            }
        }
    }
}

onItemOption("item.arcane_prayer_scroll", "read") {
    player.queue {
        if (player.getVarbit(Prayers.AUGURY_UNLOCK_VARBIT) == 1) {
            messageBox(
                "You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods. However there's nothing more for you to learn.",
            )
            return@queue
        }
        player.animate(id = 7403)
        itemMessageBox(
            "You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods! Would you like to absorb its power? <br>(Warning: This will consume the scroll.)</b>",
            item = "item.arcane_prayer_scroll",
        )
        when (options("Learn Augury", "Cancel", title = "This will consume the scroll")) {
            1 -> {
                if (player.inventory.contains("item.arcane_prayer_scroll")) {
                    player.inventory.remove(item = "item.arcane_prayer_scroll")
                    player.setVarbit(id = Prayers.AUGURY_UNLOCK_VARBIT, value = 1)
                    player.animate(id = -1)
                    itemMessageBox(
                        "You study the scroll and learn a new prayer: <col=8B0000>Augury</col>",
                        item = "item.arcane_prayer_scroll",
                    )
                }
            }
            2 -> {
                player.animate(id = -1)
            }
        }
    }
}

onItemOption("item.torn_prayer_scroll", "read") {
    player.queue {
        if (player.getVarbit(Prayers.PRESERVE_UNLOCK_VARBIT) == 1) {
            messageBox(
                "You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods. However there's nothing more for you to learn.",
            )
            return@queue
        }
        player.animate(id = 7403)
        itemMessageBox(
            "You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods! Would you like to absorb its power? <br>(Warning: This will consume the scroll.)</b>",
            item = "item.torn_prayer_scroll",
        )
        when (options("Learn Preserve", "Cancel", title = "This will consume the scroll")) {
            1 -> {
                if (player.inventory.contains("item.torn_prayer_scroll")) {
                    player.inventory.remove(item = "item.torn_prayer_scroll")
                    player.setVarbit(id = Prayers.PRESERVE_UNLOCK_VARBIT, value = 1)
                    player.animate(id = -1)
                    itemMessageBox(
                        "You study the scroll and learn a new prayer: <col=8B0000>Preserve</col>",
                        item = "item.torn_prayer_scroll",
                    )
                }
            }
            2 -> {
                player.animate(id = -1)
            }
        }
    }
}
