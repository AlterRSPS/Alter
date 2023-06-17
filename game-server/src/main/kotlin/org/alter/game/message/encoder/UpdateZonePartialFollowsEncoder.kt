package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.UpdateZonePartialFollowsMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateZonePartialFollowsEncoder : MessageEncoder<UpdateZonePartialFollowsMessage>() {

    override fun extract(message: UpdateZonePartialFollowsMessage, key: String): Number = when (key) {
        "x" -> message.x
        "z" -> message.z
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: UpdateZonePartialFollowsMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}