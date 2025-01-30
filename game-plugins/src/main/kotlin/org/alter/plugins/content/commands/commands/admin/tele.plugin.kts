package org.alter.plugins.content.commands.commands.admin

import org.alter.game.model.move.moveTo
import org.alter.game.model.priv.Privilege

onCommand("tele", Privilege.ADMIN_POWER, description = "Teleport to coordinates") {
    val values = player.getCommandArgs()
    val x = values[0].toInt()
    val y = values[1].toInt()
    val height = if (values.size > 2) values[2].toInt() else 0
    player.moveTo(x, y, height)
}
