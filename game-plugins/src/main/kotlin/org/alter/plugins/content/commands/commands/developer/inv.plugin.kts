package org.alter.plugins.content.commands.commands.developer

import net.rsprot.protocol.game.outgoing.inv.UpdateInvFull
import org.alter.game.model.priv.Privilege
import org.alter.game.rsprot.RsModObjectProvider

onCommand("inv", Privilege.DEV_POWER) {
    val values = player.getCommandArgs()
    val key = values[0].toInt()
    val items = mutableListOf<Item?>()
    for (item in 1 until values.size)
        items.add(Item(values[item].toIntOrNull() ?: 0))
    val itemarr = items.toTypedArray()
    player.write(UpdateInvFull(inventoryId = key, capacity = itemarr.size, provider = RsModObjectProvider(itemarr)))
    player.message("Added $items to inventory($key)")
}
