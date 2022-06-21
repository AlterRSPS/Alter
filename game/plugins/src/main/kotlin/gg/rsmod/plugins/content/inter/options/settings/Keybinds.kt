package gg.rsmod.plugins.content.inter.options.settings

import com.google.errorprone.annotations.Var
import gg.rsmod.plugins.api.cfg.Varbits

enum class Keybinds(val slot: Int, val varbit: Int) {
    ATTACK(22, Varbits.ATTACK_KEYBIND),
    PRAYER(23, Varbits.PRAYER_KEYBIND),
    SETTINGS(24, Varbits.SETTINGS_KEYBIND),
    SKILLS(25, Varbits.SKILLS_KEYBIND),
    MAGE(26, Varbits.MAGEBOOK_KEYBIND),
    EMOTES(27, Varbits.EMOTES_KEYBIND),
    QUEST(28, Varbits.QUEST_KEYBIND),
    FRIENDS(29, Varbits.FRIENDS_KEYBIND),
    CLAN(30, Varbits.CLAN_KEYBIND),
    INVENTORY(31, Varbits.INVENTORY_KEYBIND),
    PROFILE(32, Varbits.PROFILE_KEYBIND),
    MUSIC(33, Varbits.MUSIC_KEYBIND),
    EQUIPMENT(34, Varbits.EQUIPMENT_KEYBIND),
    LOGOUT(35, Varbits.LOGOUT_KEYBIND);

    companion object {
        val values = enumValues<Keybinds>()
    }
}