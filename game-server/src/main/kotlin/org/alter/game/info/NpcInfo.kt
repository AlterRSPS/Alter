package org.alter.game.info

import org.alter.game.model.entity.Npc

class NpcInfo(var npc: Npc) {
    val avatar = npc.avatar
    val info = avatar.extendedInfo

    fun walk(x: Int, y: Int) {
        avatar.walk(x, y)
    }
}