import gg.rsmod.plugins.content.inter.options.OptionsTab
import gg.rsmod.plugins.content.inter.options.Settings

on_button(OptionsTab.ALL_SETTINGS_INTERFACE_ID, Settings.SETTINGS_CLOSE_BUTTON_ID) {
    player.message("Closing all settings")
    player.closeInterface(OptionsTab.ALL_SETTINGS_INTERFACE_ID)
}