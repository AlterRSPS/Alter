package org.alter.plugins.content.commands.commands.developer

import net.rsprot.protocol.game.outgoing.inv.UpdateInvFull
import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.priv.Privilege
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*
import org.alter.game.rsprot.RsModObjectProvider

class InvPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
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
    }
}
