package org.alter.plugins.content.areas.lumbridge.npcs

import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.queue.QueueTask
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository


class HansPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    val dialogOptions: List<String> = listOf(
        "I'm looking for whoever is in charge of this place.",
        "I have come to kill everyone in this castle!",
        "I don't know. I'm lost. Where am I?",
        "Can you tell me how long I've been here?",
        "Nothing.",
    )

    init {
        onNpcOption("npc.hans", option = "talk-to")
        {
            player.queue { dialog() }
        }

        onNpcOption("npc.hans", option = "age")
        {
            player.queue { age() }
        }
    }


    suspend fun QueueTask.dialog() {
        val npc = player.getInteractingNpc()

        chatNpc("Hello. What are you doing here?")

        when (options(*dialogOptions.toTypedArray())) {
            1 -> {
                chatPlayer("I'm looking for whoever is in charge of this place.")
                chatNpc("Who, the Duke? He's in his study, on the first floor.")
            }

            2 -> {
                chatPlayer("I have come to kill everyone in this castle!")
                npc.forceChat("Help! Help!")
            }

            3 -> {
                chatPlayer("I don't know. I'm lost. Where am I?")
                chatNpc("You are in Lumbridge Castle, in the Kingdom of Misthalin. Across the river, the road leads north to Varrock, and to the west lies Draynor Village.")
            }

            4 -> age()
            5 -> chatPlayer("Nothing.")
        }
    }

    suspend fun QueueTask.age() {
//        val seconds = (player.playtime * 0.6).toInt()
//        val days = seconds / 86400
//        val hours = (seconds / 3600) - (days * 24)
//        val minutes = (seconds / 60) - (days * 1440) - (hours * 60)
//        val daysSinceReg = player.registryDate.until(LocalDate.now(), ChronoUnit.DAYS).toInt()
//
//        val timeString = buildString {
//            append("You've spent ")
//            append("$days${if (days != 1) " days" else " day"}")
//            append(", ")
//            append("$hours${if (hours != 1) " hours" else " hour"}")
//            append(", ")
//            append("$minutes${if (minutes != 1) " minutes" else " minute"}")
//            append(" in the world since you arrived ")
//
//            when (daysSinceReg) {
//                0 -> {
//                    append("today.")
//                }
//
//                1 -> {
//                    append("yesterday.")
//                }
//
//                else -> {
//                    append(daysSinceReg)
//                    append(" days ago.")
//                }
//            }
//        }

        chatPlayer("Can you tell me how long I've been here?")
        chatNpc("Not implemented.")
    }
}