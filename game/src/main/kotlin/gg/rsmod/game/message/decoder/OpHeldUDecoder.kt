package gg.rsmod.game.message.decoder

import gg.rsmod.game.message.MessageDecoder
import gg.rsmod.game.message.impl.OpHeldUMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpHeldUDecoder : MessageDecoder<OpHeldUMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpHeldUMessage {
        val fromComponent = values["from_component"]!!.toInt().also {
            println("fromComponent: $it")
        }
        val fromSlot = values["from_slot"]!!.toInt().also {
            println("fromSlot: $it")
        }
        val fromItem = values["from_item"]!!.toInt().also {
            println("fromItem: $it")
        }
        val toComponent = values["to_component"]!!.toInt().also {
            println("toComponent: $it")
        }
        val toSlot = values["to_slot"]!!.toInt().also {
            println("toSlot: $it")
        }
        val toItem = values["to_item"]!!.toInt().also {
            println("toItem: $it")
        }

        return OpHeldUMessage(fromComponent, fromSlot, fromItem, toComponent, toSlot, toItem)
    }
}