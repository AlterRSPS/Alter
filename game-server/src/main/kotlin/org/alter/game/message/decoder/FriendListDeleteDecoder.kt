package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.FriendListDeleteMessage

class FriendListDeleteDecoder : MessageDecoder<FriendListDeleteMessage>() {
    override fun decode(
        opcode: Int,
        opcodeIndex: Int,
        values: HashMap<String, Number>,
        stringValues: HashMap<String, String>
    ): FriendListDeleteMessage {
        val name = stringValues["name"]!!.toString()
        return FriendListDeleteMessage(name)
    }
}