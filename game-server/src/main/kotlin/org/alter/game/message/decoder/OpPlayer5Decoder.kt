package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.OpPlayer5Message

/**
 * @author Triston Plummer ("Dread")
 */
class OpPlayer5Decoder : MessageDecoder<OpPlayer5Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpPlayer5Message {
        val index = values["index"]!!.toInt()
        return OpPlayer5Message(index)
    }
}