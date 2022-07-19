package gg.rsmod.plugins.content.cmd.commands.admin

import gg.rsmod.game.model.priv.Privilege

on_command("home", Privilege.ADMIN_POWER) {
    val home = world.gameContext.home
    player.moveTo(home)
}

on_command("edge", Privilege.ADMIN_POWER) {
    player.moveTo(Tile(x = 3087, z = 3490, height = 0))
}