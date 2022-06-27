package gg.rsmod.plugins.content.inter.options.settings

import gg.rsmod.game.fs.EnumDefinitions
import gg.rsmod.game.fs.StructDefinitions
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.AttributeKey
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.cfg.Varbits
import gg.rsmod.plugins.api.ext.setVarbit

class Settings(private val world: World) {

    private val CURRENT_CATEGORY_ATTR_KEY = "adv_search_current_selected_category"
    private val CURRENT_DROPDOWN_ATTR_KEY = "adv_search_keybind"

    private val SETTINGS_ENUM_ID = 422

    private val CATEGORIES : MutableList<SettingCategory> = ArrayList()

    fun initializeCategories() {
        val categories = EnumDefinitions(SETTINGS_ENUM_ID, world).get()

        categories?.getValues()?.forEach { (k, v) ->
            val struct = StructDefinitions(v as Int, world).get()
            CATEGORIES.add(SettingCategory(struct, world).initialize())
        }
    }

    fun getCategories() : List<SettingCategory> {
        return CATEGORIES
    }

    fun getSelectedCategory(player : Player) : SettingCategory? {
        return player.attr[AttributeKey(CURRENT_CATEGORY_ATTR_KEY)]
    }

    fun setSelectedCategory(player : Player, settingCategory: SettingCategory) {
        player.attr[AttributeKey(CURRENT_CATEGORY_ATTR_KEY, temp = true)] = settingCategory
        settingCategory.getSlotId()?.let { player.setVarbit(Varbits.ALL_SETTINGS_TAB, it) }
    }

    fun getCurrentDropdownSetting(player: Player): Setting? {
        return player.attr[AttributeKey(CURRENT_DROPDOWN_ATTR_KEY)]
    }

    fun setCurrentDropdownSetting(player: Player, category: Setting?) {
        player.attr[AttributeKey(CURRENT_DROPDOWN_ATTR_KEY, temp = true)] = category
    }

    fun getCategory(slotId : Int) : SettingCategory {
        return CATEGORIES.stream()
            .filter { t ->
                t.getSlotId() == slotId
            }
            .findFirst()
            .get()
    }
}