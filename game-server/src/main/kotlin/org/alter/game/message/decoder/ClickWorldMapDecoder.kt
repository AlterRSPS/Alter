package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.ClickWorldMapMessage

/**
 * @author HolyRSPS <dagreenrs@gmail.com>
 */
class ClickWorldMapDecoder : MessageDecoder<ClickWorldMapMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): ClickWorldMapMessage {
        val data = values["data"]!!.toInt()
        return ClickWorldMapMessage(data)
    }
}