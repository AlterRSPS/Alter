package org.alter.plugins.content.mechanics.prayer

import org.alter.api.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.plugin.*

class PrayersPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onPlayerDeath {
            Prayers.deactivateAll(player)
        }

        /**
         * Deactivate all prayers on log out.
         */
        onLogout {
            Prayers.deactivateAll(player)
        }

        /**
         * Activate prayers.
         */
        Prayer.values.forEach { prayer ->
            onButton(interfaceId = 541, component = prayer.child) {
                player.queue {
                    Prayers.toggle(player, this, prayer)
                }
            }
        }

        /**
         * Prayer drain.
         */
        onLogin {
            player.timers[Prayers.PRAYER_DRAIN] = 1
        }

        onTimer(Prayers.PRAYER_DRAIN) {
            player.timers[Prayers.PRAYER_DRAIN] = 1
            Prayers.drainPrayer(player)
        }

        /**
         * Toggle quick-prayers.
         */
        onButton(interfaceId = 160, component = 19) {
            val opt = player.getInteractingOption()
            Prayers.toggleQuickPrayers(player, opt)
        }

        /**
         * Select quick-prayer.
         */
        onButton(interfaceId = 77, component = 4) {
            val slot = player.getInteractingSlot()
            val prayer = Prayer.values.firstOrNull { prayer -> prayer.quickPrayerSlot == slot } ?: return@onButton
            Prayers.selectQuickPrayer(this, prayer)
        }

        /**
         * Accept selected quick-prayer.
         */
        onButton(interfaceId = 77, component = 5) {
            player.openInterface(InterfaceDestination.PRAYER)
        }
    }
}
