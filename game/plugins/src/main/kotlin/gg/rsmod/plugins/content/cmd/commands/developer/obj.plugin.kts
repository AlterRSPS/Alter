package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("obj", Privilege.DEV_POWER, description = "Spawn object by id") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::obj 1</col>") { values ->
        val id = values[0].toInt()
        val type = if (values.size > 1) values[1].toInt() else 10
        val rot = if (values.size > 2) values[2].toInt() else 0
        val obj = DynamicObject(id, type, rot, player.tile)
        world.spawn(obj)
    }
}