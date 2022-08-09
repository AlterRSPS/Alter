package gg.rsmod.plugins.content.area.dwarven_mines.chats

spawn_npc(Npcs.DROGO_DWARF, 3040, 9847, 0, walkRadius = 15)

    on_npc_option(Npcs.DROGO_DWARF, option = "talk-to") { player.queue { dialog() } }

    on_npc_option(Npcs.DROGO_DWARF, option = "trade") { open_shop(player) }

suspend fun QueueTask.dialog() {
    chatNpc("'Ello, welcome to my Mining shop, friend!", animation = 567)
    when (options("Do you want to trade?", "Hello, shorty.", "Why don't you ever restock ores or bars?")) {
        1 -> {
            chatPlayer("Do you want to trade?")
            open_shop(player)
        }
        2 -> {
            chatPlayer("Hello, shorty", animation = 588)
            chatNpc("I may be short, but at least I've got manners.")
        }
        3 -> {
            chatPlayer("Why don't you ever restock ores and bars?")
            chatNpc("The only ores and bars I sell are those sold to me.")
        }
    }
}

fun open_shop(p: Player) { p.openShop("Drogo's Mining Emporium.") }