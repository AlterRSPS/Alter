package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.IfSetTextMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfSetTextEncoder : MessageEncoder<IfSetTextMessage>() {

    override fun extract(message: IfSetTextMessage, key: String): Number = when (key) {
        "hash" -> (message.parent shl 16) or message.child
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: IfSetTextMessage, key: String): ByteArray = when (key) {
        "text" -> {
            val data = ByteArray(message.text.length + 1)
            System.arraycopy(message.text.toByteArray(), 0, data, 0, data.size - 1)
            data[data.size - 1] = 0
            data
        }
        else -> throw Exception("Unhandled value key.")
    }
}