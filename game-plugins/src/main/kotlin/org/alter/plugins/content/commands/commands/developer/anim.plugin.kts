package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege

onCommand("anim", Privilege.DEV_POWER, description = "Play animation") {
    val args = player.getCommandArgs()
    val id = args[0].toInt()
    player.animate(id)
    player.message("Animate: $id")
}
