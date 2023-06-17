package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.IfSetPlayerHeadMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfSetPlayerHeadEncoder : MessageEncoder<IfSetPlayerHeadMessage>() {

    override fun extract(message: IfSetPlayerHeadMessage, key: String): Number = when (key) {
        "hash" -> message.hash
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: IfSetPlayerHeadMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}