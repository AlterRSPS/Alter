package gg.rsmod.plugins.content.area.dwarven_mines.chats

spawn_npc(npc = Npcs.YARSUL, x = 3031, z = 9747, walkRadius = 3, direction = Direction.SOUTH)

    on_npc_option(Npcs.YARSUL, option = "talk-to") { player.queue { dialog() } }

    on_npc_option(Npcs.YARSUL, option = "trade") { open_shop(player) }

suspend fun QueueTask.dialog() {
    chatNpc("Good day to you, welcome to my pickaxe shop. Are you<br>intrested in making a purchase?")
    when (options("Yes, please.", "No thanks.")) {
        1 -> open_shop(player)
        2 -> {
            chatPlayer("No thanks.")
            chatNpc("Suit yourself. I'll be here if you change your mind.")
        }
    }
}

fun open_shop(p: Player) { p.openShop("Yarsul's Prodigious Pickaxes") }