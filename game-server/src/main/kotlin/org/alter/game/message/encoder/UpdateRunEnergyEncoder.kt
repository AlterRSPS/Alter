package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.UpdateRunEnergyMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateRunEnergyEncoder : MessageEncoder<UpdateRunEnergyMessage>() {

    override fun extract(message: UpdateRunEnergyMessage, key: String): Number = when (key) {
        "energy" -> message.energy
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: UpdateRunEnergyMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}