import gg.rsmod.plugins.content.inter.options.settings.Settings

on_world_init {
    loadSettings(world)
}

fun loadSettings(world : World) {
    val settings = Settings(world)
    settings.initializeCategories()
    world.settings = settings
}