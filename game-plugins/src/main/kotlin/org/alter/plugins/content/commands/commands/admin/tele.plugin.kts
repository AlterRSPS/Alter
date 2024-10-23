package org.alter.plugins.content.commands.commands.admin

import org.alter.game.model.move.moveTo
import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

on_command("tele", Privilege.ADMIN_POWER, description = "Teleport to coordinates") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::tele 3200 3200</col>") { values ->
        val x = values[0].toInt()
        val y = values[1].toInt()
        val height = if (values.size > 2) values[2].toInt() else 0
        player.moveTo(x, y, height)
    }
}
