import gg.rsmod.game.model.attr.AttributeKey
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.api.cfg.Varbits
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.inter.options.OptionsTab
import gg.rsmod.plugins.content.inter.options.Settings

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
    player.attr[AttributeKey("setting_tab")] = player.getInteractingSlot()
    player.setVarbit(Varbits.ALL_SETTINGS_TAB, player.getInteractingSlot())
}

bind_all_setting(Settings.DROPDOWN) {
    player.attr[AttributeKey("Keybind")] = player.getInteractingSlot()

    if(player.getInteractingSlot() == 36) {
        player.queue {
            when(this.options(title = "Are you sure you want to reset your keybinds?", options = arrayOf("Yes.", "No."))) {
                1 -> {
                    keyBindSettings.forEach { (t, u) ->
                        player.setVarbit(u, 0)
                    }

                    player.message("Your keybinds has been reset.")
                }
                2 -> {
                    player.message("You did not reset your keybinds.")
                }
            }
        }
    }
}

bind_all_setting(Settings.DROPDOWN_CLICK) {
    val slot = player.getInteractingSlot()

    keyBindSettings.forEach { (t, u) ->
        if(player.getVarbit(u) == (slot - 2) / 3) {
            player.setVarbit(u, 0)
        }
    }

    val varbit = keyBindSettings.getOrDefault(player.attr[AttributeKey("Keybind")], 0)

    if(varbit != 0) {
        player.setVarbit(keyBindSettings.getOrDefault(player.attr[AttributeKey("Keybind")], 0), (slot - 2) / 3)
    }
}

val keyBindSettings = mapOf(
    22 to Varbits.ATTACK_KEYBIND,
    23 to Varbits.PRAYER_KEYBIND,
    24 to Varbits.SETTINGS_KEYBIND,
    25 to Varbits.SKILLS_KEYBIND,
    26 to Varbits.MAGEBOOK_KEYBIND,
    27 to Varbits.EMOTES_KEYBIND,
    28 to Varbits.QUEST_KEYBIND,
    29 to Varbits.FRIENDS_KEYBIND,
    30 to Varbits.CLAN_KEYBIND,
    31 to Varbits.INVENTORY_KEYBIND,
    32 to Varbits.PROFILE_KEYBIND,
    33 to Varbits.MUSIC_KEYBIND,
    34 to Varbits.EQUIPMENT_KEYBIND,
    35 to Varbits.LOGOUT_KEYBIND,
)