package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege

onCommand("reboot", Privilege.DEV_POWER, description = "Restart Server") {
    val values = player.getCommandArgs()
    val cycles = values[0].toInt()
    world.rebootTimer = cycles
    world.sendRebootTimer()
}
