package gg.rsmod.plugins.content.cmd.commands.admin

import gg.rsmod.game.model.priv.Privilege

on_command("home", Privilege.ADMIN_POWER, description = "Teleports you home") {
    val home = world.gameContext.home
    player.moveTo(home)
}
on_command("edge", Privilege.ADMIN_POWER, description = "Teleports you to Edgeville") {
    player.moveTo(Tile(x = 3087, z = 3499, height = 0))
}
on_command("varrock", Privilege.ADMIN_POWER, description = "Teleports you to Varrock") {
    player.moveTo(Tile(x = 3211, z = 3424, height = 0))
}
on_command("falador", Privilege.ADMIN_POWER, description = "Teleports you to Falador") {
    player.moveTo(Tile(x = 2966, z = 3379, height = 0))
}
on_command("lumbridge", Privilege.ADMIN_POWER, description = "Teleports you to Lumbridge") {
    player.moveTo(Tile(x = 3222, z = 3217, height = 0))
}
on_command("yanille", Privilege.ADMIN_POWER, description = "Teleports you to Yanille") {
    player.moveTo(Tile(x = 2606, z = 3093, height = 0))
}
on_command("gnome", Privilege.ADMIN_POWER, description = "Teleports you to Gnome Stronghold") {
    player.moveTo(Tile(x = 2461, z = 3443, height = 0))
}
