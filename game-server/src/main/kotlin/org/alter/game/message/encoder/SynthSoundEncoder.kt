package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.SynthSoundMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SynthSoundEncoder : MessageEncoder<SynthSoundMessage>() {

    override fun extract(message: SynthSoundMessage, key: String): Number = when (key) {
        "sound" -> message.sound
        "volume" -> message.volume
        "delay" -> message.delay
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: SynthSoundMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}