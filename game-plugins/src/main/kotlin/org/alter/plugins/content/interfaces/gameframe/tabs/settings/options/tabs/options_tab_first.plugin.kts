import org.alter.game.model.attr.DISPLAY_MODE_CHANGE_ATTR
import org.alter.game.model.attr.INTERACTING_SLOT_ATTR
import org.alter.game.model.interf.DisplayMode
import org.alter.plugins.content.interfaces.options.OptionsTab

val SKULL_PROTECTION_BUTTON = 5
val PLAYER_ATTACK_OPTION = 38
val NPC_ATTACK_OPTION = 39
val BRIGHTNES_BAR = 23
val ZOOM_TOGGLE_BUTTON = 44
val DISPLAY_MODE = 41
val MUTE_MUSIC = 93
val MUSIC_BAR = 104
val MUTE_SOUND = 107
val SOUND_BAR = 118
val MUTE_AREA_SOUND = 122
val AREA_SOUND_BAR = 133
val MUSIC_UNLOCK_MESSAGE = 121
val ACCEPT_AID_BUTTON = 29
val RUN_MODE_BUTTON = 30
val HOUSE_OPT_BUTTON = 31
val BOND_BUTTON = 33
val ALL_SETTINGS_BUTTON = 32

on_login {
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = 55, 0..21, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = DISPLAY_MODE, 0..21, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = BRIGHTNES_BAR, 0..21, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = 84, 1..3, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = 82, 1..4, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = 81, 1..5, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = 69, 0..21, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = SOUND_BAR, 0..21, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = PLAYER_ATTACK_OPTION, 1..5, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = NPC_ATTACK_OPTION, 1..4, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = MUSIC_BAR, 0..21, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = DISPLAY_MODE, 1..3, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = 90, 0..21, setting = InterfaceEvent.ClickOp1)
    player.setInterfaceEvents(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = AREA_SOUND_BAR, 0..21, setting = InterfaceEvent.ClickOp1)
}
fun bind_setting(child: Int, plugin: Plugin.() -> Unit) {
    on_button(interfaceId = OptionsTab.SETTINGS_INTERFACE_TAB, component = child) {
        plugin(this)
    }
}

/**
 * Changing display modes (fixed, resizable).
 */
set_window_status_logic {
    val change = player.attr[DISPLAY_MODE_CHANGE_ATTR]
    val mode = when (change) {
        2 -> if (player.getVarbit(Varbit.SIDESTONES_ARRAGEMENT_VARBIT) == 1) DisplayMode.RESIZABLE_LIST else DisplayMode.RESIZABLE_NORMAL
        else -> DisplayMode.FIXED
    }
    player.toggleDisplayInterface(mode)
}

bind_setting(child = DISPLAY_MODE) {
    val slot = player.attr[INTERACTING_SLOT_ATTR]!!
    val mode = when (slot) {
        2 -> {
            player.setVarbit(Varbit.SIDESTONES_ARRAGEMENT_VARBIT, 0)
            DisplayMode.RESIZABLE_NORMAL
        }
        3 -> {
            player.setVarbit(Varbit.SIDESTONES_ARRAGEMENT_VARBIT, 1)
            DisplayMode.RESIZABLE_LIST
        }
        else -> DisplayMode.FIXED
    }
    if(!(mode.isResizable() && player.interfaces.displayMode.isResizable()))
        player.runClientScript(3998, slot-1)
    player.toggleDisplayInterface(mode)
}

bind_setting(child = PLAYER_ATTACK_OPTION) {
    val slot = player.attr[INTERACTING_SLOT_ATTR]!!.toInt()-1
    player.setVarp(Varp.PLAYER_ATTACK_PRIORITY_VARP, slot)
}

bind_setting(child = NPC_ATTACK_OPTION) {
    val slot = player.attr[INTERACTING_SLOT_ATTR]!!.toInt()-1
    player.setVarp(Varp.NPC_ATTACK_PRIORITY_VARP, slot)
}
val BOND_INTERFACE = 65
bind_setting(child = BOND_BUTTON) {
    with (player) {
        openInterface(interfaceId = BOND_INTERFACE, dest = InterfaceDestination.MAIN_SCREEN)
        runClientScript(2498, 1, 0, 0)
        runClientScript(2524, -1, -1)
        setComponentText(interfaceId = 65, component = 25, text = "Redeem bonds for packages of <col=ffffff>14, 29 or 45 days</col>, or<br>up to <col=ffffff>a year</col> of Premier Membership.")
        runClientScript(3650, 0)
        runClientScript(733, 0,0,0,0,0,0,0,0)
    }
}
on_button(BOND_INTERFACE, 90) {
    player.setVarbit(Varbit.BOND_INTERFACE_FOCUS_TAB, 0)
}
on_button(BOND_INTERFACE, 89) {
    player.setVarbit(Varbit.BOND_INTERFACE_FOCUS_TAB, 1)
}

bind_setting(child = RUN_MODE_BUTTON) {
    player.toggleVarp(Varp.RUN_MODE_VARP)
}

bind_setting(child = ACCEPT_AID_BUTTON) {
    player.toggleVarp(Varp.ACCEPT_AID_VARP)
}
bind_setting(child = SKULL_PROTECTION_BUTTON) {
    player.toggleVarbit(Varbit.PK_PREVENT_SKULL)
}

bind_setting(63) {
    player.setVarbit(Varbit.SETTINGS_TAB_FOCUS, 0)
}
bind_setting(68) {
    player.setVarbit(Varbit.SETTINGS_TAB_FOCUS, 1)
}
bind_setting(69) {
    player.setVarbit(Varbit.SETTINGS_TAB_FOCUS, 2)
}

bind_setting(MUSIC_BAR) {
    player.setVarp(Varp.AUDIO_MUSIC_VOLUME, player.getInteractingSlot() * 5)
}

bind_setting(SOUND_BAR) {
    player.setVarp(Varp.AUDIO_SOUND_EFFECT_VOLUME, player.getInteractingSlot() * 5)
}

bind_setting(AREA_SOUND_BAR) {
    player.setVarp(Varp.AUDIO_AREA_SOUND_VOLUME, player.getInteractingSlot() * 5)
}

bind_setting(ZOOM_TOGGLE_BUTTON) {
    player.toggleVarbit(Varbit.DISABLE_ZOOM)
}

val AUDIO_MUSIC_VOLUME = AttributeKey<Int>()
val SOUND_EFFECT_VOLUME = AttributeKey<Int>()
val AREA_SOUND_VOLUME = AttributeKey<Int>()

bind_setting(MUTE_MUSIC) {
    if (player.getVarp(Varp.AUDIO_MUSIC_VOLUME) == 0) {
        player.setVarp(Varp.AUDIO_MUSIC_VOLUME, player.attr[AUDIO_MUSIC_VOLUME]!! )
    } else {
        player.attr[AUDIO_MUSIC_VOLUME] = player.getVarp(Varp.AUDIO_MUSIC_VOLUME)
        player.setVarp(Varp.AUDIO_MUSIC_VOLUME, 0)
    }
}

bind_setting(MUTE_SOUND) {
    if (player.getVarp(Varp.AUDIO_SOUND_EFFECT_VOLUME) == 0) {
        player.setVarp(Varp.AUDIO_SOUND_EFFECT_VOLUME, player.attr[SOUND_EFFECT_VOLUME]!! )
    } else {
        player.attr[SOUND_EFFECT_VOLUME] = player.getVarp(Varp.AUDIO_SOUND_EFFECT_VOLUME)
        player.setVarp(Varp.AUDIO_SOUND_EFFECT_VOLUME, 0)
    }
}

bind_setting(MUTE_AREA_SOUND) {
    if (player.getVarp(Varp.AUDIO_AREA_SOUND_VOLUME) == 0) {
        player.setVarp(Varp.AUDIO_AREA_SOUND_VOLUME, player.attr[AREA_SOUND_VOLUME]!! )
    } else {
        player.attr[AREA_SOUND_VOLUME] = player.getVarp(Varp.AUDIO_AREA_SOUND_VOLUME)
        player.setVarp(Varp.AUDIO_AREA_SOUND_VOLUME, 0)
    }
}