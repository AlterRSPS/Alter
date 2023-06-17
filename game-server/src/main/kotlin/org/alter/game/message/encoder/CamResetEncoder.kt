package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.CamResetMessage

/**
 * @author Bmyte
 */
class CameraResetEncoder : MessageEncoder<CamResetMessage>() {

    override fun extract(message: CamResetMessage, key: String): Number = throw Exception("Unhandled value key.")

    override fun extractBytes(message: CamResetMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}