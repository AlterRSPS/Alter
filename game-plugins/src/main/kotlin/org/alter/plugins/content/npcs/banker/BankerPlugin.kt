package org.alter.plugins.content.npcs.banker

import org.alter.api.InterfaceDestination
import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import org.alter.plugins.content.interfaces.bank.openBank

class BankerPlugin(
    r: PluginRepository, world: World, server: Server
) : KotlinPlugin(r, world, server) {

    private val bankers = listOf(
        "npc.banker_1479",
        "npc.banker_1480",
        "npc.banker_2897",
    )

    init {
        bankers.forEach { banker ->
            onNpcOption(npc = banker, option = "talk-to", lineOfSightDistance = 2) {
                player.queue {
                    dialog(this)
                }
            }
            onNpcOption(npc = banker, option = "bank", lineOfSightDistance = 2) {
                player.openBank()
            }
            onNpcOption(npc = banker, option = "collect", lineOfSightDistance = 2) {
                openCollect(player)
            }
        }
    }

    suspend fun dialog(it: QueueTask) {
        it.chatNpc("Good day, how may I help you?")
        when (options(it)) {
            1 -> it.player.openBank()
            2 -> openPin(it.player)
            3 -> openCollect(it.player)
            4 -> whatIsThisPlace(it)
        }
    }

    suspend fun options(it: QueueTask): Int = it.options(
        "I'd like to access my bank account, please.",
        "I'd like to check my PIN settings.",
        "I'd like to collect items.",
        "What is this place?",
    )

    private suspend fun whatIsThisPlace(it: QueueTask) {
        it.chatNpc("This is a branch of the Bank of Gielinor. We have<br>branches in many towns.", animation = 568)
        it.chatPlayer("And what do you do?", animation = 554)
        it.chatNpc(
            "We will look after your items and money for you.<br>Leave your valuables with us if you want to keep them<br>safe.",
            animation = 569,
        )
    }

    private fun openCollect(p: Player) {
        p.setInterfaceUnderlay(color = -1, transparency = -1)
        p.openInterface(interfaceId = 402, dest = InterfaceDestination.MAIN_SCREEN)
    }

    private fun openPin(p: Player) {
        p.setInterfaceUnderlay(color = -1, transparency = -1)
        p.openInterface(interfaceId = 14, dest = InterfaceDestination.MAIN_SCREEN)
    }

}
