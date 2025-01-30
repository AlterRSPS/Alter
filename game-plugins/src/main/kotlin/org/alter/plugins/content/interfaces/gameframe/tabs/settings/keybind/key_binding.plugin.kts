package org.alter.plugins.content.interfaces.keybind

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
