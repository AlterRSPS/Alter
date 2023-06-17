package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.OpPlayer6Message

/**
 * @author Triston Plummer ("Dread")
 */
class OpPlayer6Decoder : MessageDecoder<OpPlayer6Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpPlayer6Message {
        val index = values["index"]!!.toInt()
        return OpPlayer6Message(index)
    }
}