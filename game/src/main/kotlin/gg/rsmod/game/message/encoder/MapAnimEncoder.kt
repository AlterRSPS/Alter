package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.MapAnimMessage

/**
 * @author bmyte <bmytescape@gmail.com>
 */
class MapAnimEncoder : MessageEncoder<MapAnimMessage>() {

    override fun extract(message: MapAnimMessage, key: String): Number = when (key) {
        "id" -> message.id.also {
            println("MapAnim:ID:${message.id}");
        }
        "height" -> message.height.also {
            println("MapAnim:Height:${message.height}");
        }
        "delay" -> message.delay.also {
            println("MapAnim:delay:${message.delay}");
        }
        "tile" -> message.tile.also {
            println("MapAnim:Title:${message.tile}");
        }
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: MapAnimMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}