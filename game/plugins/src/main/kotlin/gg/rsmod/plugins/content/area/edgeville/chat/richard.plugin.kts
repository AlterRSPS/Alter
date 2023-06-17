package gg.rsmod.plugins.content.area.edgeville.chat

spawn_npc(npc = Npcs.RICHARD_2200, x = 3102, z = 3518, walkRadius = 2)

on_npc_option(npc = Npcs.RICHARD_2200, option = "talk-to") { player.queue { chat() } }

on_npc_option(npc = Npcs.RICHARD_2200, option = "trade") { open_shop(player) }

suspend fun QueueTask.chat() {
    chatNpc("Hello there, are you interested in buying one of my<br>special capes?", animation = 568)
    options()
}

suspend fun QueueTask.options() {
    when (options("What's so special about your capes?", "Yes please!", "No thanks.")) {
        1 -> {
            chatPlayer("What's so special about your capes?", animation = 554)
            chatNpc("Ahh well they make it less likely that you'll accidently<br>attack anyone wearing the same cape as you and easier<br>to attack everyone else. They also make it easier to<br>distinguish people who're wearing the same cape as you", animation = 570)
            chatNpc("from everyone else. They're very useful when out in<br>the wilderness with friends or anyone else you don't<br>want to harm.", animation = 569)
            chatNpc("So would you like to buy one?", animation = 567)
            when (options("Yes please!", "No thanks.")) {
                1 -> open_shop(player)
                2 -> no_thanks()
            }
        }
        2 -> open_shop(player)
        3 -> no_thanks()
    }
}

fun open_shop(p: Player) {
    p.openShop("Richard's Wilderness Cape Shop.")
}

suspend fun QueueTask.no_thanks() {
    chatPlayer("No thanks.", animation = 588)
}