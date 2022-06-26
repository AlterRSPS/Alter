import gg.rsmod.game.Server.Companion.logger
import gg.rsmod.plugins.content.inter.options.settings.Settings

on_world_init {
    loadSettings(world)
}

fun loadSettings(world : World) {
    logger.info("Initiated Settings")
    val settings = Settings(world)
    settings.initializeCategories()
    world.settings = settings
    logger.info("Loaded Settings")
}