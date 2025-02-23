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
                    dialog(player, this)
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

    suspend fun dialog(player: Player, it: QueueTask) {
        it.chatNpc(player, "Good day, how may I help you?")
        when (options(player, it)) {
            1 -> player.openBank()
            2 -> openPin(player)
            3 -> openCollect(player)
            4 -> whatIsThisPlace(player, it)
        }
    }

    suspend fun options(player: Player, it: QueueTask): Int =
        it.options(
            player,
            "I'd like to access my bank account, please.",
            "I'd like to check my PIN settings.",
            "I'd like to collect items.",
            "What is this place?",
        )

    suspend fun whatIsThisPlace(player: Player, it: QueueTask) {
        it.chatNpc(
            player,
            "This is a branch of the Bank of Gielinor. We have<br>branches in many towns.",
            animation = 568
        )
        it.chatPlayer(player, "And what do you do?", animation = 554)
        it.chatNpc(
            player,
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
