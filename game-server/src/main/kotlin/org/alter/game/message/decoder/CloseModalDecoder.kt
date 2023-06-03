package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.CloseModalMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CloseModalDecoder : MessageDecoder<CloseModalMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): CloseModalMessage {
        return CloseModalMessage()
    }
}