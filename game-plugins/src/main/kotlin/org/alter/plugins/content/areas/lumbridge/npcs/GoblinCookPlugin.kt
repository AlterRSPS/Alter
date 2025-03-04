package org.alter.plugins.content.areas.lumbridge.npcs

import org.alter.api.ext.chatNpc
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.Direction
import org.alter.game.model.World
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class GoblinCookPlugin (
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        spawnNpc(npc = "npc.goblin_cook_4851", x = 3246, z = 3246, walkRadius = 2, direction = Direction.WEST)

        onNpcOption("npc.goblin_cook_4851", option = "talk-to") {
            player.queue { dialog(player) }
        }
    }

    suspend fun QueueTask.dialog(player: Player) {
        chatNpc(player, "No 'fraid, Bitsy don't bite. She only itsy!")
    }
}
