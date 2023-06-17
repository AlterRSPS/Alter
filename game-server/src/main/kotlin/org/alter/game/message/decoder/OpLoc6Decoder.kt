package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.OpLoc6Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpLoc6Decoder : MessageDecoder<OpLoc6Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpLoc6Message {
        val id = values["id"]!!.toInt()
        return OpLoc6Message(id)
    }
}