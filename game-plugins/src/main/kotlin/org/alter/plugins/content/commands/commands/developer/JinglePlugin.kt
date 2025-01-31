package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege

onCommand("jingle", Privilege.DEV_POWER, description = "Play jingle by id") {
    val values = player.getCommandArgs()
    val id = values[0].toInt()
    player.playJingle(id)
    player.message("Jingle: $id")
}
