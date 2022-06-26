import gg.rsmod.game.Server.Companion.logger
import gg.rsmod.game.fs.DefinitionSet
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
    world.getSettings().setSelectedCategory(player, world.getSettings().getCategory(player.getInteractingSlot()))
}

bind_all_setting(Settings.DROPDOWN) {
    println(world.getSettings().getSelectedCategory(player)?.getName())

    /*player.attr[AttributeKey("keybind")] = player.getInteractingSlot()

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
    }*/
}

bind_all_setting(Settings.DROPDOWN_CLICK) {
    /*val slot = player.getInteractingSlot()

    keyBindSettings.forEach { (t, u) ->
        if(player.getVarbit(u) == (slot - 2) / 3) {
            player.setVarbit(u, 0)
        }
    }

    val varbit = keyBindSettings.getOrDefault(player.attr[AttributeKey("keybind")], 0)

    if(varbit != 0) {
        player.setVarbit(keyBindSettings.getOrDefault(player.attr[AttributeKey("keybind")], 0), (slot - 2) / 3)
    }*/
}