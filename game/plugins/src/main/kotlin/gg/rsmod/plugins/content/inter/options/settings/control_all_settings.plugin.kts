import gg.rsmod.plugins.api.cfg.Varbits
import gg.rsmod.plugins.content.inter.options.OptionsTab
import gg.rsmod.plugins.content.inter.options.Settings

fun bind_all_setting(child: Int, plugin: Plugin.() -> Unit) {
    on_button(interfaceId = OptionsTab.ALL_SETTINGS_INTERFACE_ID, component = child) {
        plugin(this)
    }
}

var keybindSlot = 0

bind_all_setting(Settings.KEYBIND) {
    keybindSlot = player.getInteractingSlot()

    if(keybindSlot == 36) {
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

bind_all_setting(Settings.KEYBIND_DROPDOWN) {
    val slot = player.getInteractingSlot()

    keyBindSettings.forEach { (t, u) ->
        if(player.getVarbit(u) == (slot - 2) / 3) {
            player.setVarbit(u, 0)
        }
    }

    val varbit = keyBindSettings.getOrDefault(keybindSlot, 0)

    if(varbit != 0) {
        player.setVarbit(keyBindSettings.getOrDefault(keybindSlot, 0), (slot - 2) / 3)
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