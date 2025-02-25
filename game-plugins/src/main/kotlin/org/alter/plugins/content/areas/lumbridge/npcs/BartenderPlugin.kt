package org.alter.plugins.content.areas.lumbridge.npcs

import org.alter.api.ext.chatNpc
import org.alter.api.ext.chatPlayer
import org.alter.api.ext.options
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.Direction
import org.alter.game.model.World
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class BartenderPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        spawnNpc("npc.bartender_7546", x = 3232, z = 3241, direction = Direction.WEST)

        onNpcOption("npc.bartender_7546", option = "talk-to", lineOfSightDistance = 4) {
            player.queue { dialog(player) }
        }
    }

    private suspend fun QueueTask.dialog(player: Player) {
        chatNpc(player, "Welcome to the Sheared Ram. What can I do for you?")
        when (options(player, "I'll have a beer please.", "Heard any rumors recently?", "Nothing, I'm fine.")) {
            1 -> {
                chatPlayer(player, "I'll have a beer please.")
                chatNpc(player, "That'll be two coins please.")
                if (player.inventory.contains("item.coins_995")) {
                    player.inventory.remove("item.coins_995", 2)
                    player.inventory.add("item.beer", 1)
                } else {
                    chatPlayer(player, "Oh dear, I don't seem to have enough money.")
                }
            }

            2 -> {
                chatPlayer(player, "Heard any rumors recently?")
                chatNpc(player, "One of the patrons here is looking for treasure<br><br>apparently. A chap byu the name of Veos.")
            }

            3 -> chatPlayer(player, "Nothing, I'm fine.")
        }
    }
}