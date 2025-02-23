package org.alter.plugins.content.areas.lumbridge.npcs

import org.alter.api.GameframeTab
import org.alter.api.cfg.Animation
import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository

class PrayerTutorPlugin (
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        spawnNpc("npc.prayer_tutor", x = 3242, z = 3214, walkRadius = 3)

        onNpcOption("npc.prayer_tutor", option = "talk-to") {
            player.queue { dialog(player) }
        }
    }


    private suspend fun QueueTask.dialog(player: Player) {
        chatPlayer(player, "Good day, sister.")
        chatNpc(player, "Greetings, ${player.username}. Can I help you with anything,<br><br> today?")
        when (options(player, "How can I train my prayer?", "What is prayer useful for?", "No, thank you.")) {
            1 -> howToTrain(player)
            2 -> prayerUseful(player)
            3 -> noThanks(player)
        }
    }

    private suspend fun QueueTask.noThanks(player: Player) {
        chatPlayer(player, "No, thank you.", animation = Animation.CHAT_QUIZ1)
        chatNpc(player, "Very well. Saradomin be with you!")
    }

    private suspend fun QueueTask.howToTrain(player: Player) {
        chatPlayer(player, "How can I train my prayer?")
        chatNpc(player, "The most common way to train prayer is by either<br>burying bones, or offering them to the gods at some<br>kind of an altar.")
        chatNpc(player,
            "Lots of adventurers build such altars in their own<br>homes, or there are a few frequent places of warship<br>around the world.",
        )
        chatNpc(player,
            "Different kinds of bones will help you train faster.<br>Generally speaking, the bigger they are and the more<br>frightening a creature they come from, the better they are for it.",
        )
        chatNpc(player, "Is there anything else you would like to know?")
        when (options(player, "What is prayer useful for?", "No, thank you.")) {
            1 -> prayerUseful(player)
            2 -> noThanks(player)
        }
    }

    private suspend fun QueueTask.prayerUseful(player: Player) {
        chatPlayer(player, "What is prayer useful for?")
        chatNpc(player,
            "The gods look kindly upon their devout followers. There<br>are all kinds of denefits they may provide, if you pray<br>for them!",
        )
        chatPlayer(player, "Really? What kind of benefits?")
        chatNpc(player,
            "They could help you in combat, help your wonds to<br>heal more quickly, protect your belongings... There's a<br>lot they can do for you!",
        )
        player.focusTab(GameframeTab.PRAYER)
        chatNpc(player, "You can find out more by looking in your prayer book.")
        chatPlayer(player, "Wow! That sounds great.")
        chatNpc(player,
            "You need to be careful that your prayers don't run<br>out, though. You can get prayer potions to help you<br>recharge, or you can pray at an altar whenever one's<br>nearby.",
        )
        chatNpc(player, "is there anything else you would like to know?")
        when (options(player, "How can I train my prayer?", "No, thank you.")) {
            1 -> howToTrain(player)
            2 -> noThanks(player)
        }
    }

}