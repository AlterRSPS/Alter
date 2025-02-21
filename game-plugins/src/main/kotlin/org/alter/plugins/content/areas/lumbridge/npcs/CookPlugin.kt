package org.alter.plugins.content.areas.lumbridge.npcs

import org.alter.api.ext.chatNpc
import org.alter.api.ext.chatPlayer
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.Direction
import org.alter.game.model.World
import org.alter.game.model.queue.QueueTask
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class CookPlugin (
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        spawnNpc("npc.cook_4626", x = 3209, z = 3215, direction = Direction.SOUTH)

        onNpcOption("npc.cook_4626", option = "talk-to") {
            player.queue { dialog() }
        }
    }

    suspend fun QueueTask.dialog() {
        chatPlayer("Hello there, cook!")
        chatPlayer("Do you have anything for me?")
        chatNpc("Sorry, not yet.")
    }
}