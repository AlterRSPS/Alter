package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.OpObj3Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpObj3Decoder : MessageDecoder<OpObj3Message>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpObj3Message {
        val item = values["item"]!!.toInt()
        val x = values["x"]!!.toInt()
        val z = values["z"]!!.toInt()
        val movementType = values["movement_type"]!!.toInt()

        return OpObj3Message(item, x, z, movementType)
    }
}