package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.LocAddChangeMessage

/**
 * @author Tom <rspsmods@gmail.com>
 * @TODO Lod or Loc? I think someone misspeled that D:
 */
class LodAddChangeEncoder : MessageEncoder<LocAddChangeMessage>() {

    override fun extract(message: LocAddChangeMessage, key: String): Number = when (key) {
        "tile" -> message.tile.also {
            println("LodAddChange:Hash:${message.tile}");
        }
        "settings" -> message.settings.also {
            println("LodAddChange:settings:${message.settings}");
        }
        "id" -> message.id.also {
            println("LodAddChange:Id:${message.id}");
        }
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: LocAddChangeMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}