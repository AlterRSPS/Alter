package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.OpPlayer8Message

/**
 * @author Triston Plummer ("Dread")
 */
class OpPlayer8Decoder : MessageDecoder<OpPlayer8Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpPlayer8Message {
        val index = values["index"]!!.toInt()
        return OpPlayer8Message(index)
    }
}