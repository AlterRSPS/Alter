package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.OpNpc1Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpc1Decoder : MessageDecoder<OpNpc1Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpNpc1Message {
        val index = values["index"]!!.toInt()
        val movement = values["movement_type"]!!.toInt()
        return OpNpc1Message(index, movement)
    }
}