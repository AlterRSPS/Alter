package org.alter.plugins.content.commands.commands.admin

import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.priv.Privilege
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class FoodPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        onCommand("food", Privilege.ADMIN_POWER, description = "Fills your inventory with food") {
            player.inventory.add(item = "item.manta_ray", amount = player.inventory.freeSlotCount)
        }

    }
}
