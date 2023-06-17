package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.MapAnimMessage

/**
 * @author bmyte <bmytescape@gmail.com>
 */
class MapAnimEncoder : MessageEncoder<MapAnimMessage>() {

    override fun extract(message: MapAnimMessage, key: String): Number = when (key) {
        "id" -> message.id
        "height" -> message.height
        "delay" -> message.delay
        "tile" -> message.tile
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: MapAnimMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}