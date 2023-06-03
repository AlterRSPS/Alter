package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.MoveGameClickMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MoveGameClickDecoder : MessageDecoder<MoveGameClickMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): MoveGameClickMessage {
        val x = values["x"]!!.toInt()
        val z = values["z"]!!.toInt()
        val type = values["movement_type"]!!.toInt()

        return MoveGameClickMessage(x, z, type)
    }
}