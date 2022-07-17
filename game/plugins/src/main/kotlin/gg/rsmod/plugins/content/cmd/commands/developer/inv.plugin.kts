package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.message.impl.UpdateInvFullMessage
import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage

on_command("inv", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::inv invKey itemIds ...</col>") { values ->
        val key = values[0].toInt()
        val items = mutableListOf<Item>()
        for(item in 1 until values.size)
            items.add(Item(values[item].toIntOrNull() ?: 0))
        player.write(UpdateInvFullMessage(containerKey = key, items = items.toTypedArray()))
        player.message("Added $items to inventory($key)")
    }
}