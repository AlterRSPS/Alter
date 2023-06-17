package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.LocDelMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class LocDelEncoder : MessageEncoder<LocDelMessage>() {

    override fun extract(message: LocDelMessage, key: String): Number = when (key) {
        "tile" -> message.tile
        "settings" -> message.settings
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: LocDelMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}