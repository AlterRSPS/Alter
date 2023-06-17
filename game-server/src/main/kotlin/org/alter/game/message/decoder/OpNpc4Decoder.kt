package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.OpNpc4Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpc4Decoder : MessageDecoder<OpNpc4Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpNpc4Message {
        val index = values["index"]!!.toInt()
        val movement = values["movement_type"]!!.toInt()
        return OpNpc4Message(index, movement)
    }
}