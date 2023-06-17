package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.ClientCheatMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClientCheatDecoder : MessageDecoder<ClientCheatMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): ClientCheatMessage {
        return ClientCheatMessage(stringValues["command"]!!)
    }
}
