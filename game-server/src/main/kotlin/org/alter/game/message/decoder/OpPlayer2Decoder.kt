package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.OpPlayer2Message

/**
 * @author Triston Plummer ("Dread")
 */
class OpPlayer2Decoder : MessageDecoder<OpPlayer2Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpPlayer2Message {
        val index = values["index"]!!.toInt()
        return OpPlayer2Message(index)
    }
}