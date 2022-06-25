import gg.rsmod.game.Server.Companion.logger
import gg.rsmod.plugins.content.inter.options.settings.Settings

on_world_init {
    loadSettings(world)
}

fun loadSettings(world : World) {
    logger.info("Initiated Settings")
    val settings = Settings(world)
    settings.initializeCategories()
    logger.info("Loaded Settings")

    settings.getCategories().forEach { it ->
        logger.info("Category: {}", it.getName())
        logger.info("Settings count: {}", it.getSettings().size)

        it.getSettings().forEach { e ->
            logger.info("Setting: {}", e.getName())
            logger.info("StructID: {}", e.getStructId())
            logger.info("slotId: {}", e.getPosition())
        }

        logger.info("-------------------------------------------------")
    }
}