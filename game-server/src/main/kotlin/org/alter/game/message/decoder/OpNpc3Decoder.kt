package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.OpNpc3Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpc3Decoder : MessageDecoder<OpNpc3Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpNpc3Message {
        val index = values["index"]!!.toInt()
        val movement = values["movement_type"]!!.toInt()
        return OpNpc3Message(index, movement)
    }
}