package org.alter.game.message.encoder

import org.alter.game.message.Message
import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.IfSetModelMessage

class IfSetModelEncoder : MessageEncoder<IfSetModelMessage>() {

    override fun extract(message: IfSetModelMessage, key: String): Number =
        when (key) {
        "hash" -> message.hash
        "model_id" -> message.model_id
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: IfSetModelMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}