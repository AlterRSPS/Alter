package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.LocAddChangeMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class LodAddChangeEncoder : MessageEncoder<LocAddChangeMessage>() {

    override fun extract(message: LocAddChangeMessage, key: String): Number = when (key) {
        "tile" -> message.tile
        "settings" -> message.settings
        "id" -> message.id
        "unknown" -> 31
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: LocAddChangeMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}