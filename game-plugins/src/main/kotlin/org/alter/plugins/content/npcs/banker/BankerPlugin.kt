package org.alter.plugins.content.npcs.banker

import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*
import org.alter.plugins.content.interfaces.bank.openBank

class BankerPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        arrayOf("npc.banker_1479", "npc.banker_1480").forEach { banker ->
            onNpcOption(npc = banker, option = "talk-to", lineOfSightDistance = 2) {
                player.queue {
                    dialog(this)
                }
            }
            onNpcOption(npc = banker, option = "bank", lineOfSightDistance = 2) {
                player.openBank()
            }
            onNpcOption(npc = banker, option = "collect", lineOfSightDistance = 2) {
                open_collect(player)
            }
        }
    }

    suspend fun dialog(it: QueueTask) {
        it.chatNpc("Good day, how may I help you?")
        when (options(it)) {
            1 -> it.player.openBank()
            2 -> open_pin(it.player)
            3 -> open_collect(it.player)
            4 -> what_is_this_place(it)
        }
    }

    suspend fun options(it: QueueTask): Int =
        it.options(
            "I'd like to access my bank account, please.",
            "I'd like to check my PIN settings.",
            "I'd like to collect items.",
            "What is this place?",
        )

    suspend fun what_is_this_place(it: QueueTask) {
        it.chatNpc("This is a branch of the Bank of Gielinor. We have<br>branches in many towns.", animation = 568)
        it.chatPlayer("And what do you do?", animation = 554)
        it.chatNpc(
            "We will look after your items and money for you.<br>Leave your valuables with us if you want to keep them<br>safe.",
            animation = 569,
        )
    }

    fun open_collect(p: Player) {
        p.setInterfaceUnderlay(color = -1, transparency = -1)
        p.openInterface(interfaceId = 402, dest = InterfaceDestination.MAIN_SCREEN)
    }

    fun open_pin(p: Player) {
        p.setInterfaceUnderlay(color = -1, transparency = -1)
        p.openInterface(interfaceId = 14, dest = InterfaceDestination.MAIN_SCREEN)
    }

}
