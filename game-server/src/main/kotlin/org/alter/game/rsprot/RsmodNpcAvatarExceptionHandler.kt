package org.alter.game.rsprot

import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcAvatarExceptionHandler
import org.alter.game.model.World

class RsmodNpcAvatarExceptionHandler(val world: World) : NpcAvatarExceptionHandler {
    override fun exceptionCaught(
        index: Int,
        exception: Exception,
    ) {
        val npc = world.npcs[index] ?: error("Npc not found at index: $index")
        world.remove(npc)
    }
}
