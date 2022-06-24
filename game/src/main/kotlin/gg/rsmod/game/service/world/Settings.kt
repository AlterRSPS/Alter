package gg.rsmod.game.service.world

import gg.rsmod.game.Server.Companion.logger
import gg.rsmod.game.fs.EnumDefinitions
import gg.rsmod.game.fs.StructDefinitions
import gg.rsmod.game.model.World

class Settings(world : World) {

    private val SETTINGS_ENUM_ID = 422

    private val world = world

    private val CATEGORIES : MutableList<SettingCategory> = ArrayList()

    fun initializeCategories() {
        val categories = EnumDefinitions(SETTINGS_ENUM_ID, world).get()

        categories?.getValues()?.forEach { (k, v) ->
            val struct = StructDefinitions(v as Int, world).get()
            logger.info("{}", k)
            CATEGORIES.add(SettingCategory(struct, world).initialize())
        }
    }

    fun getCategories() : List<SettingCategory> {
        return CATEGORIES
    }
}