package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.ClanJoinChatLeaveChatMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClanJoinChatLeaveChatDecoder : MessageDecoder<ClanJoinChatLeaveChatMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): ClanJoinChatLeaveChatMessage {
        val name = stringValues["name"]!!
        return ClanJoinChatLeaveChatMessage(name)
    }
}