import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.inter.options.OptionsTab
import gg.rsmod.plugins.content.inter.options.Settings
import gg.rsmod.plugins.content.inter.options.settings.Setting
import gg.rsmod.plugins.content.inter.options.settings.SettingType
import gg.rsmod.plugins.content.inter.options.settings.SettingVariables

fun bind_all_setting(child: Int, plugin: Plugin.() -> Unit) {
    on_button(interfaceId = OptionsTab.ALL_SETTINGS_INTERFACE_ID, component = child) {
        plugin(this)
    }
}

on_button(OptionsTab.ALL_SETTINGS_INTERFACE_ID, Settings.SETTINGS_CLOSE_BUTTON_ID) {
    player.message("Closing all settings")
    player.closeInterface(OptionsTab.ALL_SETTINGS_INTERFACE_ID)
    player.closeComponent(parent = 162, child = 599)
}

bind_all_setting(Settings.TAB_CLICK) {
    world.getSettings().setSelectedCategory(player, world.getSettings().getCategory(player.getInteractingSlot()))
}

bind_all_setting(Settings.CLICK) {
    val category = world.getSettings().getSelectedCategory(player)
    val setting = category?.getSettings()?.stream()?.filter { t -> t.getPosition() == player.getInteractingSlot() }?.findFirst()?.get()

    when(setting?.getType()) {
        SettingType.TOGGLE -> {
            toggle(player, setting)
        }
    }
}

fun toggle(player: Player, setting: Setting) {
    val currentValue: Int = SettingVariables.getVariableValue(setting, player)
    SettingVariables.setVariableValue(setting, player, if (currentValue == 1) 0 else 1)
}