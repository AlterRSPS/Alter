package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.OpPlayer3Message

/**
 * @author Triston Plummer ("Dread")
 */
class OpPlayer3Decoder : MessageDecoder<OpPlayer3Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpPlayer3Message {
        val index = values["index"]!!.toInt()
        return OpPlayer3Message(index)
    }
}