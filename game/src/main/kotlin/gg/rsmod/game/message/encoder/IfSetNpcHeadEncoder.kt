package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.IfSetNpcHeadMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfSetNpcHeadEncoder : MessageEncoder<IfSetNpcHeadMessage>() {

    override fun extract(message: IfSetNpcHeadMessage, key: String): Number = when (key) {
        "hash" -> message.hash.also {
            println("IfSetNpcHead:Hash:${message.hash}");
        }
        "npc" -> message.npc.also {
            println("IfSetNpcHead:npc:${message.npc}");
        }
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: IfSetNpcHeadMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}