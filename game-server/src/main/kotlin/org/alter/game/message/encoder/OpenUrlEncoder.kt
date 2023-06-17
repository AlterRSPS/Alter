package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.OpenUrlMessage

/**
 * @author bmyte
 */
class OpenUrlEncoder : MessageEncoder<OpenUrlMessage>() {

    override fun extract(message: OpenUrlMessage, key: String): Number = throw Exception("Unhandled value key.")

    override fun extractBytes(message: OpenUrlMessage, key: String): ByteArray = when (key) {
        "url" -> {
            val data = ByteArray(message.url.length)
            System.arraycopy(message.url.toByteArray(), 0, data, 0, data.size)
            data
        }
        else -> throw Exception("Unhandled value key.")
    }
}