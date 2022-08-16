package gg.rsmod.plugins.content.area.lumbridge.chat

spawn_npc(Npcs.COOK_4626, x = 3209, z = 3215, direction = Direction.SOUTH )

on_npc_option(Npcs.COOK_4626, option = "talk-to") {
    player.queue { dialog() }
}

suspend fun QueueTask.dialog() {
    chatPlayer("Hello there, cook!")
    chatPlayer("Do you have anything for me?")
    chatNpc("Sorry, not yet.")
}