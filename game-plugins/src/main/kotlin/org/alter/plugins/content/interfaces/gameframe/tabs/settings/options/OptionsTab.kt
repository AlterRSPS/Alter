package org.alter.plugins.content.interfaces.options

import org.alter.game.model.bits.BitStorage

/**
 * @author Tom <rspsmods@gmail.com>
 */
object OptionsTab {
    const val SETTINGS_INTERFACE_TAB = 116
    const val SETTINGS_TAB_SELECTED_VARBIT = 9683
    const val ALL_SETTINGS_BUTTON_ID = 75
    const val ALL_SETTINGS_INTERFACE_ID = 134

    /**
     * Control Settings Tab
     */
    const val CONTROL_SETTINGS_BUTTON_ID = 65
    const val ACCEPT_AID_BUTTON_ID = 29
    const val RUN_MODE_BUTTON_ID = 73

    const val HOUSE_OPT_BUTTON_ID = 28
    const val HOUSE_OPT_INTERFACE_ID = 370

    const val BOND_BUTTON_ID = 76
    const val BONDS_INTERFACE_ID = 65
    const val REDEEM_BONDS_INTERFACE_ID = 66

    /**
     * Audio Settings Tab
     */
    const val AUDIO_SETTINGS_BUTTON_ID = 70
    const val MUSIC_UNLOCK_BUTTON_ID = 25
    const val MUSIC_UNLOCK_MESSAGE_VARBIT = 10078

    /**
     * Display Settings Tab
     */
    const val DISPLAY_SETTINGS_BUTTON_ID = 71
    const val DISPLAY_MODE_DROPDOWN_ID = 25

    val GAME_NOTIFICATIONS = BitStorage(persistenceKey = "game_notifications")
}
