package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.FriendListAddMessage

class FriendListAddDecoder : MessageDecoder<FriendListAddMessage>() {
    override fun decode(
        opcode: Int,
        opcodeIndex: Int,
        values: HashMap<String, Number>,
        stringValues: HashMap<String, String>
    ): FriendListAddMessage {
        val name = stringValues["name"]!!.toString()
        return FriendListAddMessage(name)
    }
}