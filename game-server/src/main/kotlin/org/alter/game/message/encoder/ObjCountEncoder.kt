package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.ObjCountMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ObjCountEncoder : MessageEncoder<ObjCountMessage>() {

    override fun extract(message: ObjCountMessage, key: String): Number = when (key) {
        "item" -> message.item
        "old_amount" -> message.oldAmount
        "new_amount" -> message.newAmount
        "tile" -> message.tile
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: ObjCountMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}