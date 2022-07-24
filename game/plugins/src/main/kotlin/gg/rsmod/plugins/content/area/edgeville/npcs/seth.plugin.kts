package gg.rsmod.plugins.content.area.edgeville.npcs

val npc = Npcs.SETH
on_npc_option(npc = npc, option = "Talk-to", lineOfSightDistance = 0) {
    player.queue {
        NpcPlaceHolder().dialog(this)
    }
}
