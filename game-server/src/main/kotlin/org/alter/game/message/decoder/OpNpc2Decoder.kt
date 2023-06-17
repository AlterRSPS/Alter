package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.OpNpc2Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpc2Decoder : MessageDecoder<OpNpc2Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpNpc2Message {
        val index = values["index"]!!.toInt()
        val movement = values["movement_type"]!!.toInt()
        return OpNpc2Message(index, movement)
    }
}