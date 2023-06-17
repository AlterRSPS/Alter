package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.MidiSongMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MidiSongEncoder : MessageEncoder<MidiSongMessage>() {

    override fun extract(message: MidiSongMessage, key: String): Number = when (key) {
        "id" -> message.id
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: MidiSongMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}