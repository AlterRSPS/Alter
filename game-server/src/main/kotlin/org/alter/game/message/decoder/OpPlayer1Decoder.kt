package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.OpPlayer1Message

/**
 * @author Triston Plummer ("Dread")
 */
class OpPlayer1Decoder : MessageDecoder<OpPlayer1Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpPlayer1Message {
        val index = values["index"]!!.toInt()
        return OpPlayer1Message(index)
    }
}