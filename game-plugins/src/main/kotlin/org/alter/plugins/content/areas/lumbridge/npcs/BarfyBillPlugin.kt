package org.alter.plugins.content.areas.lumbridge.npcs

import org.alter.api.Skills
import org.alter.api.ext.chatNpc
import org.alter.api.ext.chatPlayer
import org.alter.api.ext.options
import org.alter.api.ext.player
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class BarfyBillPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        spawnNpc("npc.barfy_bill", x = 3243, z = 3237, walkRadius = 3, height = 0)

        onNpcOption("npc.barfy_bill", option = "talk-to") {
            player.queue { dialog(player) }
        }
    }

    suspend fun QueueTask.dialog(player: Player) {
        chatPlayer(player, "Hello there.", animation = 588)
        chatNpc(player, "Oh! Hello there.", animation = 588)
        when (options(player, "Who are you?", "Can you teach me about Canoeing?")) {
            1 -> {
                chatPlayer(player, "Who are you?", animation = 588)
                chatNpc(player, "My name is Ex Sea Captain Barfy Bill.", animation = 588)
                chatPlayer(player, "Ex sea captain?", animation = 554)
                chatNpc(
                    player,
                    "Yeah, I bought a lovely ship and was planning to make<br>a fortune running her as a merchant vessel.",
                    animation = 611
                )
                chatPlayer(player, "Why are you not still sailing?", animation = 554)
                chatNpc(
                    player,
                    "Chronic sea sickness. My first, and only, voyage was<br>spent dry heaving over the rails.",
                    animation = 611
                )
                chatNpc(
                    player,
                    "If I had known about the sea sickness I could have<br>saved myself a lot of money.",
                    animation = 589
                )
                chatPlayer(player, "What are you up to now then?", animation = 575)
                chatNpc(
                    player,
                    "Well my ship had a little fire related problem.<br>Fortunately it was well insured.",
                    animation = 593
                )
                chatNpc(
                    player,
                    "Anyway, I don't have to work anymore so I've taken to<br>canoeing on the river.",
                    animation = 589
                )
                chatNpc(player, "I don't get river sick!", animation = 567)
                chatNpc(player, "Would you like to know how to make a canoe?", animation = 554)

                when (options(player, "Yes", "No")) {
                    1 -> teach(player)
                    2 -> chatPlayer(player, "No thanks, not right now.", animation = 588)
                }
            }

            2 -> teach(player)
        }
    }

    private suspend fun QueueTask.teach(player: Player) {
        if (player.getSkills().getCurrentLevel(Skills.WOODCUTTING) < 12) {
            chatPlayer(player, "Could you teach me about canoes?", animation = 554)
            chatNpc(player, "Well, you don't look like you have the skill to make a<br>canoe.", animation = 589)
            chatNpc(player, "You need to have at least level 12 woodcutting.", animation = 588) // TODO
            chatNpc(
                player,
                "Once you are able to make a canoe it makes travel<br>along the river much quicker!",
                animation = 589
            )
        } else {
            chatPlayer(player, "Could you teach me about canoes?", animation = 554)
            chatNpc(player, "It's really quite simple. Just walk down to that tree on<br>the bank and chop it down.")
            chatNpc(player, "When you have done that you can shape the log<br>further with your axe to make a canoe.")
            if (player.getSkills().getCurrentLevel(Skills.WOODCUTTING) < 27) {
                chatNpc(player, "Hah! I can tell just by looking that you lack talent in<br>woodcutting.")
                chatPlayer(player, "What do you mean?")
                chatNpc(player, "No Callouses! No Splinters! No camp fires littering the<br>trail behind you.")
                chatNpc(
                    player,
                    "Anyway, the only 'canoe' you can make is a log. You'll<br>be able to travel 1 stop along the river with a log canoe."
                )
            } else if (player.getSkills().getCurrentLevel(Skills.WOODCUTTING) < 42) {
                chatNpc(
                    player,
                    "With your skill in woodcutting you could make my<br>favourite canoe, the Dugout. They might not be the<br>best canoe on the river, but they get you where you're<br>going.",
                )
                chatPlayer(player, "How far will I be able to go in a Dugout canoe?")
                chatNpc(player, "You will be able to travel 2 stops on the river.")
            } else if (player.getSkills().getCurrentLevel(Skills.WOODCUTTING) < 57) {
                chatNpc(player, "The best canoe you can make is a Stable Dugout, one<br>step beyond a normal Dugout.")
                chatNpc(player, "With a Stable Dugout you can travel to any place on<br>the river.")
                chatPlayer(player, "Even into the Wilderness?")
                chatNpc(
                    player,
                    "Not likely! I've heard tell of a man up near Edgeville<br>who claims he can use a Waka to get up into the<br>Wilderness.",
                )
                chatNpc(player, "I can't think why anyone would wish to venture into<br>that hellish landscape though.")
            } else if (player.getSkills().getCurrentLevel(Skills.WOODCUTTING) >= 57) {
                chatNpc(player, "Hoo! You look like you know which end of an axe is<br>which!")
                chatNpc(
                    player,
                    "You can easily build one of those Wakas. Be careful if<br>you travel into the Wilderness though."
                )
                chatNpc(player, "I've heard tell of great evil in that blasted wasteland.")
                chatPlayer(player, "Thanks for the warning Bill.")
            }
        }
    }
}
