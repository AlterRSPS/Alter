package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

onCommand("anim", Privilege.DEV_POWER, description = "Play animation") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::anim 1</col>") { values ->
        val id = values[0].toInt()
        player.animate(id)
        player.message("Animate: $id")
    }
}
