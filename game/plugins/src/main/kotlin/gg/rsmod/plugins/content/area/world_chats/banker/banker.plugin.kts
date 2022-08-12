package gg.rsmod.plugins.content.area.world_chats.banker

import gg.rsmod.plugins.content.inter.bank.openBank


arrayOf(Npcs.BANKER_2897, Npcs.BANKER_2898, Npcs.BANKER_1633, Npcs.BANKER_1634, Npcs.BANKER_1613, Npcs.BANKER_1618).forEach { banker ->
    on_npc_option(npc = banker, option = "talk-to", lineOfSightDistance = 2) {
        player.queue { dialog() }
    }
    on_npc_option(npc = banker, option = "bank", lineOfSightDistance = 2) {
        player.openBank()
    }
    on_npc_option(npc = banker, option = "collect", lineOfSightDistance = 2) {
        open_collect(player)
    }
}

suspend fun QueueTask.dialog() {
    chatNpc("Good day, how may I help you?")
    when (options("I'd like to access my bank account, please.", "I'd like to check my PIN settings.", "I'd like to collect items.", "I'd like to buy more bank slots.", "What is this place?")) {
        1 -> player.openBank()
        2 -> open_pin(player)
        3 -> open_collect(player)
        4 -> more_bank_slots()
        5 -> {
            chatPlayer("What is this place?")
            chatNpc("This is a branch of the Bank of Gielinor. We have<br><br>branches in many towns.", animation = 568)
            chatPlayer("And what do you do?", animation = 554)
            chatNpc("We will look after your items and money for you.<br>Leave your valuables with us if you want to keep them<br>safe.", animation = 569)
        }
    }
}

// TODO: 7/26/2022 add in more slots interface
suspend fun QueueTask.more_bank_slots() {
    chatPlayer("I'd like to buy more bank slots.")
    chatNpc("I can sell you up to 360 additional bank slots in sets of<br>40. How many are you interested in buying?", animation = 568)
    chatPlayer("Actually, I've changed my mind.", animation = 554)
}

fun open_collect(p: Player) {
    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.openInterface(interfaceId = 402, dest = InterfaceDestination.MAIN_SCREEN)
}

fun open_pin(p: Player) {
    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.openInterface(interfaceId = 14, dest = InterfaceDestination.MAIN_SCREEN)
}