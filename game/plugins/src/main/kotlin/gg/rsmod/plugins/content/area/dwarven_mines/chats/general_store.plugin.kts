package gg.rsmod.plugins.content.area.dwarven_mines.chats

spawn_npc(Npcs.DWARF_5904, 2997, 9826, 0, walkRadius = 11)

    on_npc_option(Npcs.DWARF_5904, option = "talk-to") { player.queue { dialog() } }

    on_npc_option(Npcs.DWARF_5904, option = "trade") { open_shop(player) }

suspend fun QueueTask.dialog() {
    chatNpc("Can I help you at all?")
    when (options("Yes, please, what are you selling?", "No thanks.")) {
        1 -> open_shop(player)
        2 -> chatPlayer("No thanks.")
    }
}

fun open_shop(p: Player) { p.openShop("Dwarven shopping store") }