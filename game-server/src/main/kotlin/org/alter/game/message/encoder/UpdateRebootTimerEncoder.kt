package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.UpdateRebootTimerMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateRebootTimerEncoder : MessageEncoder<UpdateRebootTimerMessage>() {

    override fun extract(message: UpdateRebootTimerMessage, key: String): Number = when (key) {
        "cycles" -> message.cycles
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: UpdateRebootTimerMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}