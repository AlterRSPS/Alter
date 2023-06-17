package gg.rsmod.plugins.content.area.lumbridge.chat

spawn_npc(npc= Npcs.GOBLIN_COOK_4851, x = 3246, z = 3246, walkRadius = 2, direction = Direction.WEST)

on_npc_option(Npcs.GOBLIN_COOK_4851, option = "talk-to") {
    player.queue { dialog() }
}

suspend fun QueueTask.dialog() {
    chatNpc("No 'fraid, Bitsy don't bite. She only itsy!")

}