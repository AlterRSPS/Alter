package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege


onCommand("song", Privilege.DEV_POWER, description = "Play song by id") {
    val values = player.getCommandArgs()
        val id = values[0].toInt()
        player.playSong(id)
        player.message("Song: $id")
}
