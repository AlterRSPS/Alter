package gg.rsmod.plugins.content.area.dwarven_mines.chats

spawn_npc(npc = Npcs.HENDOR, x = 3031, z = 9747, walkRadius = 3, direction = Direction.SOUTH)

    on_npc_option(Npcs.HENDOR, option = "talk-to") { player.queue { dialog() } }

    on_npc_option(Npcs.HENDOR, option = "trade") { open_shop(player) }

suspend fun QueueTask.dialog() {
    chatNpc("Hello there! If you have any ore to trade I'm always<br>buying.")
    when (options("Let's trade.", "Why don't you ever restock your shop?", "Goodbye.")) {
        1 -> open_shop(player)
        2 -> {
            chatPlayer("Why don't you ever restock your shop?")
            chatNpc("The only ores I sell are the ones that are sold to me.")
            chatNpc("Anything else?")
            when (options("Let's trade.", "Goodbye."))  {
                1 -> open_shop(player)
                2 -> {
                    chatPlayer("See you later.")
                    chatNpc("Until next time.")
                }
            }
        }
        3 -> {
            chatPlayer("See you later.")
            chatNpc("Until next time.")
        }
    }
}

fun open_shop(p: Player) { p.openShop("Hendor's Awesome Ores") }