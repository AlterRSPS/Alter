package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.OpNpc6Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpc6Decoder : MessageDecoder<OpNpc6Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpNpc6Message {
        val npcId = values["npc_id"]!!.toInt()
        return OpNpc6Message(npcId)
    }
}