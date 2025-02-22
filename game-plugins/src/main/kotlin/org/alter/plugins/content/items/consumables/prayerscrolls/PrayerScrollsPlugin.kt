package org.alter.plugins.content.items.consumables.prayerscrolls

import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*
import org.alter.plugins.content.mechanics.prayer.Prayers
import org.alter.rscm.RSCM.getRSCM

class PrayerScrollsPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onItemOption("item.dexterous_prayer_scroll", "read") {
            player.queue {
                if (player.getVarbit(Prayers.RIGOUR_UNLOCK_VARBIT) == 1) {
                    messageBox(player,
                        "You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods. However there's nothing more for you to learn.",
                    )
                    return@queue
                }
                player.animate(id = 7403)
                itemMessageBox(player,
                    "You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods! Would you like to absorb its power? <br>(Warning: This will consume the scroll.)</b>",
                    item = "item.dexterous_prayer_scroll",
                )
                when (options(player, "Learn Rigour", "Cancel", title = "This will consume the scroll")) {
                    1 -> {
                        if (player.inventory.contains(getRSCM("item.dexterous_prayer_scroll"))) {
                            player.inventory.remove(item = "item.dexterous_prayer_scroll")
                            player.setVarbit(id = Prayers.RIGOUR_UNLOCK_VARBIT, value = 1)
                            player.animate(id = -1)
                            itemMessageBox(player,
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
                    messageBox(player,
                        "You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods. However there's nothing more for you to learn.",
                    )
                    return@queue
                }
                player.animate(id = 7403)
                itemMessageBox(player,
                    "You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods! Would you like to absorb its power? <br>(Warning: This will consume the scroll.)</b>",
                    item = "item.arcane_prayer_scroll",
                )
                when (options(player, "Learn Augury", "Cancel", title = "This will consume the scroll")) {
                    1 -> {
                        if (player.inventory.contains("item.arcane_prayer_scroll")) {
                            player.inventory.remove(item = "item.arcane_prayer_scroll")
                            player.setVarbit(id = Prayers.AUGURY_UNLOCK_VARBIT, value = 1)
                            player.animate(id = -1)
                            itemMessageBox(player,
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
                    messageBox(player,
                        "You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods. However there's nothing more for you to learn.",
                    )
                    return@queue
                }
                player.animate(id = 7403)
                itemMessageBox(player,
                    "You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods! Would you like to absorb its power? <br>(Warning: This will consume the scroll.)</b>",
                    item = "item.torn_prayer_scroll",
                )
                when (options(player, "Learn Preserve", "Cancel", title = "This will consume the scroll")) {
                    1 -> {
                        if (player.inventory.contains("item.torn_prayer_scroll")) {
                            player.inventory.remove(item = "item.torn_prayer_scroll")
                            player.setVarbit(id = Prayers.PRESERVE_UNLOCK_VARBIT, value = 1)
                            player.animate(id = -1)
                            itemMessageBox(player,
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
    }
}
