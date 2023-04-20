package gg.rsmod.plugins.content.commands.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.commands.Commands_plugin.Command.tryWithUsage


on_command("song", Privilege.DEV_POWER, description = "Play song by id") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::song 1</col>") { values ->
        val id = values[0].toInt()
        player.playSong(id)
        player.message("Song: $id")
    }
}