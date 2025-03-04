package org.alter.plugins.content.areas.lumbridge.npcs

import org.alter.api.ext.chatNpc
import org.alter.api.ext.chatPlayer
import org.alter.api.ext.itemMessageBox
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.Direction
import org.alter.game.model.World
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class CraftingTutorPlugin (
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        spawnNpc("npc.crafting_tutor", x = 3211, z = 3212, 1, 1, Direction.WEST)

        onNpcOption(npc = "npc.crafting_tutor", option = "talk-to") {
            player.queue { dialog(player) }
        }
    }

    suspend fun QueueTask.dialog(player: Player) {
        chatPlayer(player,"Can you teach me the basics of crafting please?", animation = 554)
        chatNpc(player,
            "Firstly, you should know that not all places associated<br>with crafting will be marked on your minimap. Some<br>take quite a bit of hunting down to find, don't lose<br>heart!",
            animation = 591,
        )
        chatPlayer(player,"I see... so where should I start?", animation = 554)
        chatNpc(player,
            "I suggest you help Farmer Fred out with his sheep<br>shearing, you can find him northwest of Lumbridge at<br>his sheep farm, this will give you experience using the<br>spinning wheel here in this room.",
            animation = 570,
        )
        chatNpc(player,
            "It's fairly easy, simply gather your wool from sheep in<br>the field near Lumbridge using the shears sold in<br>general stores, then click on the spinning wheel and you<br>will be given a choice of what to spin. Right-clicking on",
            animation = 570,
        )
        chatNpc(player,"the choices will allow you to make multiple spins and<br>therefore save you time.", animation = 568)
        chatNpc(player,"When you have a full inventory, take it to the bank,<br>you can find it on the roof of this very castle.", animation = 568)
        itemMessageBox(player,
            "To find a bank, look for this symbol on your minimap<br>after climbing the stairs of the Lumbridge Castle to the<br>top. There are banks all over the world with this symbol.",
            item = "item.null_5080",
            amountOrZoom = 400,
        )
    }
}