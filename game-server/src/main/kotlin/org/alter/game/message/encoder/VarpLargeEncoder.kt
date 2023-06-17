package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.VarpLargeMessage
import org.alter.game.message.impl.VarpSmallMessage

/**
 * Responsible for extracting values from the [VarpSmallMessage] based on keys.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class VarpLargeEncoder : MessageEncoder<VarpLargeMessage>() {

    override fun extract(message: VarpLargeMessage, key: String): Number = when (key) {
        "id" -> message.id
        "value" -> message.value
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: VarpLargeMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}