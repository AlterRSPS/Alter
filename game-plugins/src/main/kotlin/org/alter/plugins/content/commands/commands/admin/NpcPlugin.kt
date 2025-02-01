package org.alter.plugins.content.commands.commands.admin

import org.alter.api.ext.getCommandArgs
import org.alter.api.ext.message
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.entity.Npc
import org.alter.game.model.priv.Privilege
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class NpcPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("npc", Privilege.ADMIN_POWER, description = "Spawn Npc") {
            val values = player.getCommandArgs()
            val id = values[0].toInt()
            val npc = Npc(id, player.tile, world)
            player.message("NPC: $id , on x:${player.tile.x} y:${player.tile.z}")
            world.spawn(npc)
        }
    }
}
