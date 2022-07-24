package gg.rsmod.plugins.content.area.edgeville.npcs

val npc = Npcs.JIMMY_DAZZLER
on_npc_option(npc = npc, option = "Talk-to", lineOfSightDistance = 0) {
    player.queue {
        NpcPlaceHolder().dialog(this)
    }
}
