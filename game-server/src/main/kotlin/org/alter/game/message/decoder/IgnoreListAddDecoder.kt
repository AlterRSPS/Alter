package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.IgnoreListAddMessage

class IgnoreListAddDecoder : MessageDecoder<IgnoreListAddMessage>() {
    override fun decode(
        opcode: Int,
        opcodeIndex: Int,
        values: HashMap<String, Number>,
        stringValues: HashMap<String, String>
    ): IgnoreListAddMessage {
        val name = stringValues["name"]!!.toString()
        return IgnoreListAddMessage(name)
    }
}