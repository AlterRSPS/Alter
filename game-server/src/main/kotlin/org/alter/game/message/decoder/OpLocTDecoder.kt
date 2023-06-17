package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.OpLocTMessage

class OpLocTDecoder : MessageDecoder<OpLocTMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpLocTMessage {
        val x = values["x"]!!.toInt()
        val z = values["z"]!!.toInt()
        val item = values["item"]!!.toInt()
        val slot = values["slot"]!!.toInt()
        val obj = values["obj"]!!.toInt()
        val movementType = values["movement_type"]!!.toInt()
        val hash = values["hash"]!!.toInt()
        return OpLocTMessage(x = x, z = z, slot = slot, item = item, obj = obj, movementType = movementType, hash = hash)
    }
}