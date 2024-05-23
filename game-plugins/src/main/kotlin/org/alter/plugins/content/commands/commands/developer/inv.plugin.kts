package org.alter.plugins.content.commands.commands.developer

import net.rsprot.protocol.game.outgoing.inv.UpdateInvFull
import org.alter.game.model.priv.Privilege
import org.alter.game.rsprot.RsModObjectProvider
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

on_command("inv", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::inv invKey itemIds ...</col>") { values ->
        val key = values[0].toInt()
        val items = mutableListOf<Item?>()
        for(item in 1 until values.size)
            items.add(Item(values[item].toIntOrNull() ?: 0))
        val itemarr = items.toTypedArray()
//        player.write(UpdateInvFull(inventoryId = key, capacity = itemarr.size, provider = RsModObjectProvider(itemarr)))
        player.message("Added $items to inventory($key)")
    }
}