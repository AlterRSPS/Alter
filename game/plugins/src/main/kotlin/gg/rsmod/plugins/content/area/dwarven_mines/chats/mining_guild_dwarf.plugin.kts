package gg.rsmod.plugins.content.area.dwarven_mines.chats

spawn_npc(npc = Npcs.DWARF_7712, x = 3045, z = 9758, walkRadius = 3)

on_npc_option(Npcs.DWARF_7712, option = "talk-to") { player.queue { dialog() } }

suspend fun QueueTask.dialog() {
    chatNpc("Welcome to the Mining Guild.<br><br>Can I help you with anything?")
    when (options("What have you got in the Guild?", "What do you dwarves do with the ore you mine?", "No thanks, I'm fine.")) {
        1 -> whatsInThere()
        2 -> oreYouMine()
        3 -> chatPlayer("No thanks, I'm fine.")
    }
}

suspend fun QueueTask.whatsInThere () {
    chatPlayer("What have you got in the guild?")
    chatNpc("All sorts of things!<br>There's plenty of coal rocks along with some iron,<br>mithril and adamantite as well.")
    chatNpc("There's no better mining site anywhere!")
    when (options("What do you dwarves do with the ore you mine?", "No thanks, I'm fine.")) {
        1 -> oreYouMine()
        2 -> chatPlayer("No thanks, I'm fine.")
    }
}

suspend fun QueueTask.oreYouMine () {
    chatPlayer("What do you dwarves do with the ore you mine?")
    chatNpc("What do you think? We smelt it into bars, smith the<br>metal to make armour and weapons, then we exchange<br>them for goods and services.")
    chatPlayer("I don't see many dwarves<br><br>selling armour or weapons here.")
    chatNpc("No, this is only a mining outpost. We dwarves don't<br>much like to settle in human cities. Most of the ore is<br>carted off to keldagrim, the great dwarven city.<br>They've got a special blast furnace up there - it makes")
    chatNpc("smelting the ore so much easier. There are plenty of<br>dwarven traders working in keldagrim. Anyway, can I<br>help you with anything else?")
    when (options("What have you go in the guild?", "No thanks, I'm fine")) {
        1 -> whatsInThere()
        2 -> chatPlayer("No thanks, I'm fine.")
    }
}