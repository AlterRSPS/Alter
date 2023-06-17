package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.UpdateRunWeightMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateRunWeightEncoder : MessageEncoder<UpdateRunWeightMessage>() {

    override fun extract(message: UpdateRunWeightMessage, key: String): Number = when (key) {
        "weight" -> message.weight
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: UpdateRunWeightMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}