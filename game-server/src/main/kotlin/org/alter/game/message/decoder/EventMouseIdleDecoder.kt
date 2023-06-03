package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.EventMouseIdleMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventMouseIdleDecoder : MessageDecoder<EventMouseIdleMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): EventMouseIdleMessage {
        return EventMouseIdleMessage()
    }
}