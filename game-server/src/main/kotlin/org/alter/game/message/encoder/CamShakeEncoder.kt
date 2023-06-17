package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.CamShakeMessage

/**
 * @author Bmyte
 */
class CamShakeEncoder : MessageEncoder<CamShakeMessage>() {

    override fun extract(message: CamShakeMessage, key: String): Number = when (key) {
        "index" -> message.index
        "left" -> message.left
        "center" -> message.center
        "right" -> message.right
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: CamShakeMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}