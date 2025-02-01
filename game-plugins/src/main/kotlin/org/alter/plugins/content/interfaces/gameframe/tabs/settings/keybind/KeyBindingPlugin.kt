package org.alter.plugins.content.interfaces.gameframe.tabs.settings.keybind

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
import org.alter.plugins.content.interfaces.keybind.Hotkey
import org.alter.plugins.content.interfaces.keybind.KeyBinding

class KeyBindingPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        /**
         * Set 'focused' hotkey.
         */
        Hotkey.values.forEach { hotkey ->
            onButton(interfaceId = 121, component = hotkey.child) {
                player.setVarbit(KeyBinding.FOCUSED_HOTKEY_VARBIT, hotkey.id)
            }
        }

        /**
         * Set hotkey value.
         */
        onButton(interfaceId = 121, component = 111) {
            val focused = player.getVarbit(KeyBinding.FOCUSED_HOTKEY_VARBIT)
            val hotkey = Hotkey.values.firstOrNull { h -> h.id == focused } ?: return@onButton

            val slot = player.getInteractingSlot()
            KeyBinding.set(player, hotkey, slot)
        }

        /**
         * Restore defaults.
         */
        onButton(interfaceId = 121, component = 104) {
            Hotkey.values.forEach { hotkey ->
                player.setVarbit(hotkey.varbit, hotkey.defaultValue)
            }
        }

        onButton(interfaceId = 121, component = 103) {
            player.toggleVarbit(KeyBinding.ESC_CLOSES_INTERFACES)
        }
    }
}
