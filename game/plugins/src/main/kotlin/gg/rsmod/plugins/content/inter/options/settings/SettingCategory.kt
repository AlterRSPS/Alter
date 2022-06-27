package gg.rsmod.plugins.content.inter.options.settings

import gg.rsmod.game.Server.Companion.logger
import gg.rsmod.game.fs.EnumDefinitions
import gg.rsmod.game.fs.StructDefinitions
import gg.rsmod.game.model.World

class SettingCategory(private val struct : StructDefinitions, private val world: World) {
    private val SETTINGS_CATEGORY_ID_PARAM = 743
    private val SETTINGS_CATEGORY_NAME_PARAM = 744
    private val SETTINGS_CATEGORY_SETTINGS_LIST_PARAM = 745

    private var slotId: Int? = null
    private var name: String? = null
    private val settings : MutableList<Setting> = ArrayList()

    internal fun initialize() : SettingCategory {
        slotId = struct.getParamAsInt(SETTINGS_CATEGORY_ID_PARAM)
        name = struct.getParamAsString(SETTINGS_CATEGORY_NAME_PARAM)

        val options = struct.getParamAsInt(SETTINGS_CATEGORY_SETTINGS_LIST_PARAM)
            ?.let { EnumDefinitions(it, world).get() }

        options?.getValues()?.forEach { (k, v) ->
            val struct = StructDefinitions(v as Int, world).get()
            settings.add(Setting(struct, k))
        }

        return this
    }

    fun getSlotId() : Int? {
        return slotId
    }

    fun getName() : String? {
        return name
    }

    fun getSettings() : MutableList<Setting> {
        return settings
    }

    fun getSetting(position : Int) : Setting {
        return settings.stream()
            .filter { t ->
                t.getPosition() == position
            }
            .findFirst()
            .get()
    }
}