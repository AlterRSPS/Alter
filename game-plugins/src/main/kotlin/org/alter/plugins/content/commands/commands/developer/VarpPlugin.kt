package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege

onCommand("varp", Privilege.DEV_POWER, description = "Set varp to amount") {
    val args = player.getCommandArgs()
        val varp = args[0].toInt()
        val state = args[1].toInt()
        val oldState = player.getVarp(varp)
        player.setVarp(varp, state)
        player.message("Set varp (<col=801700>$varp</col>) from <col=801700>$oldState</col> to <col=801700>${player.getVarp(varp)}</col>")
}

onCommand("getvarp") {
    val args = player.getCommandArgs()
    val varpState = player.getVarp(args[0].toInt())
    player.message("${args[0]} Varp state: $varpState")
}
