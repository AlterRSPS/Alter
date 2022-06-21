import gg.rsmod.plugins.content.inter.options.OptionsTab
import gg.rsmod.plugins.content.inter.options.Settings
import gg.rsmod.plugins.content.inter.options.settings.Keybinds

fun bind_all_setting(child: Int, plugin: Plugin.() -> Unit) {
    on_button(interfaceId = OptionsTab.ALL_SETTINGS_INTERFACE_ID, component = child) {
        plugin(this)
    }
}

var keybindSlot = 0

bind_all_setting(Settings.KEYBIND) {
    keybindSlot = player.getInteractingSlot()
}

bind_all_setting(Settings.KEYBIND_DROPDOWN) {
    val slot = player.getInteractingSlot()

    Keybinds.values.forEach { keybind ->
        if(keybind.slot == keybindSlot) {
            player.setVarbit(keybind.varbit, (slot - 2) / 3)
        }
    }
}