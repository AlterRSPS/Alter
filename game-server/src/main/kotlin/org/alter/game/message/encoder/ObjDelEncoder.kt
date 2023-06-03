package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.ObjDelMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ObjDelEncoder : MessageEncoder<ObjDelMessage>() {

    override fun extract(message: ObjDelMessage, key: String): Number = when (key) {
        "item" -> message.item
        "tile" -> message.tile
        "amount" -> message.amount
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: ObjDelMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}