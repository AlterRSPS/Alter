package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.DetectModifiedClientMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DetectModifiedClientDecoder : MessageDecoder<DetectModifiedClientMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): DetectModifiedClientMessage {
        val dummy = values["dummy"]!!.toInt()
        return DetectModifiedClientMessage(dummy)
    }
}