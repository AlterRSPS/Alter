package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.attr.CHANGE_LOGGING
import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

on_command("getvarbit", Privilege.DEV_POWER, description = "Get varbit state") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::getvarbit 5451</col>") { values ->
        val varbit = values[0].toInt()
        val state = player.getVarbit(varbit)
        player.message("Get varbit (<col=801700>$varbit</col>): <col=801700>$state</col>")
    }
}

on_command(
    "logchanges",
    Privilege.DEV_POWER,
    description = "Will log all varbits/varps when it gets changed you will see in-game messages.",
) {
    val varbitLogging = !(player.attr[CHANGE_LOGGING] ?: false)
    player.attr[CHANGE_LOGGING] = varbitLogging
    player.message("Change Logging: ${if (varbitLogging) "<col=178000>Enabled</col>" else "<col=801700>Disabled</col>"}")
}
