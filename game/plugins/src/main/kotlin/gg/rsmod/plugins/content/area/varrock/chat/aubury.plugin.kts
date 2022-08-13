package gg.rsmod.plugins.content.area.varrock.chat

spawn_npc(Npcs.AUBURY_11435, 3253, 3402, 0, 2)

arrayOf(Npcs.AUBURY_11434, Npcs.AUBURY_11435, Npcs.AUBURY).forEach { aubury ->

    if (if_npc_has_option(npc = aubury, option = "trade")) {
        on_npc_option(npc = aubury, option = "trade") { open_shop(player) }
    }

    if (if_npc_has_option(npc = aubury, option = "talk-to")) {
        on_npc_option(npc = aubury, option = "talk-to", lineOfSightDistance = 1) { player.queue { dialog() } }
    }

    if (if_npc_has_option(npc = aubury, option = "teleport")) {
        on_npc_option(npc = aubury, option = "teleport", lineOfSightDistance = 1) {
            player.queue {
                val npc = player.getInteractingNpc()
                player.lock = LockState.FULL
                npc.forceChat("Senventior Disthine Molenko")
                npc.graphic(108, 10)
                wait(3)
                player.graphic(110, 125)
                wait(2)
                player.moveTo(2912, 4833, 0)
                player.lock = LockState.NONE
            }
        }
    }
}

suspend fun QueueTask.dialog() {
    chatNpc("Do you want to buy some runes?")
    when (options()) {
        1 -> about_your_cape()
        2 -> open_shop(player)
        3 -> no_thank_you()
        4 -> teleport_me()
    }
}

suspend fun QueueTask.options(): Int = options("Can you tell me about your cape?", "Yes please!", "Oh, it's a rune shop. No thank you then.", "Can you teleport me to the Rune Essence?")

suspend fun QueueTask.about_your_cape() {
    chatNpc("Certainly! Skillcapes are a symbol of achievement. Only people who have mastered a skill and reached level 99 can get their hands on them and gain the benefits they carry.", animation = 568)
    chatNpc("The Cape of Runescape has been upgraded with each talisman, allowing you to access all Runecrafting altars. Is there anything else I can help you with?", animation = 554)
}

fun open_shop(p: Player) {
    p.openShop("Aubury's Rune Shop.")
}

suspend fun QueueTask.no_thank_you() {
    chatPlayer("Oh, it's a rune shop. No thank you, then.", animation = 568)
    chatNpc("Well, if you find someone who does want runes, please send them my way.", animation = 554)
}

suspend fun QueueTask.teleport_me() {
    chatPlayer("Can you teleport me to the Rune Essence?", animation = 568)
}