package gg.rsmod.game.service.world

import gg.rsmod.game.Server.Companion.logger
import gg.rsmod.game.fs.StructDefinitions
import gg.rsmod.game.model.World

class SettingCategory(struct : StructDefinitions, world: World) {
    private val SETTINGS_CATEGORY_ID_PARAM = 743
    private val SETTINGS_CATEGORY_NAME_PARAM = 744
    private val SETTINGS_CATEGORY_SETTINGS_LIST_PARAM = 745

    private val struct = struct
    private var slotId = null
    private var name = null
    private val settings : MutableList<Setting> = ArrayList()

    internal fun initialize() : SettingCategory {
        logger.info("Setting slotId: {}", struct.getParamAsString(SETTINGS_CATEGORY_ID_PARAM))
        logger.info("Setting name: {}", struct.getParamAsString(SETTINGS_CATEGORY_NAME_PARAM))
        logger.info("Settings list enum: {}", struct.getParamAsInt(SETTINGS_CATEGORY_SETTINGS_LIST_PARAM))

        return this
    }
}