package org.alter.game.sync.segment

import org.alter.game.sync.SynchronizationSegment
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcNoMovementSegment : SynchronizationSegment {

    override fun encode(buf: GamePacketBuilder) {
        buf.putBits(2, 0)
    }
}