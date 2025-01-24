package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege

onCommand("interface", Privilege.DEV_POWER, description = "Open interface by id") {
    val values = player.getCommandArgs()
    val component = values[0].toInt()
    player.openInterface(component, InterfaceDestination.MAIN_SCREEN)
    player.message("Opening interface <col=801700>$component</col>")
}
