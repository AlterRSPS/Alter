import gg.rsmod.game.Server.Companion.logger
import gg.rsmod.game.model.attr.INTERACTING_SLOT_ATTR
import gg.rsmod.game.model.interf.DisplayMode
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
        SettingType.DROPDOWN -> {
            dropdown(player, setting)
        }
        SettingType.KEYBIND -> {
            dropdown(player, setting)
        }
    }
}

bind_all_setting(Settings.DROPDOWN_CLICK) {
    world.getSettings().getCurrentDropdownSetting(player)
        ?.let { SettingVariables.setVariableValue(it, player, player.getInteractingSlot() / 3) }
}

fun dropdown(player: Player, setting: Setting) {
    world.getSettings().setCurrentDropdownSetting(player, world.getSettings().getSelectedCategory(player)?.getSetting(player.getInteractingSlot()))
}

fun toggle(player: Player, setting: Setting) {
    val currentValue: Int = SettingVariables.getVariableValue(setting, player)
    SettingVariables.setVariableValue(setting, player, if (currentValue == 1) 0 else 1)
}

on_button(OptionsTab.ALL_SETTINGS_INTERFACE_ID, Settings.SHOW_INFORMATION) {
    player.toggleVarbit(Varbits.SETTINGS_INTERFACE_INFORMATION)
}


bind_all_setting(child = OptionsTab.DISPLAY_MODE_DROPDOWN_ID) {
    //val slot = player.getInteractingSlot()
    val slot = player.attr[INTERACTING_SLOT_ATTR]!!
    val mode = when (slot) {
        2 -> {
            player.setVarbit(Varbits.SIDESTONES_ARRAGEMENT_VARBIT, 0)
            DisplayMode.RESIZABLE_NORMAL
        }
        3 -> {
            player.setVarbit(Varbits.SIDESTONES_ARRAGEMENT_VARBIT, 1)
            DisplayMode.RESIZABLE_LIST
        }
        else -> DisplayMode.FIXED
    }
    if(!(mode.isResizable() && player.interfaces.displayMode.isResizable()))
        player.runClientScript(3998, slot-1)
    player.toggleDisplayInterface(mode)
}


