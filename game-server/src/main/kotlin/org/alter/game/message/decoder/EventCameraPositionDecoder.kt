package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.EventCameraPositionMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventCameraPositionDecoder : MessageDecoder<EventCameraPositionMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): EventCameraPositionMessage {
        val mouseX = values["pitch"]!!.toInt()
        val mouseY = values["yaw"]!!.toInt()
        return EventCameraPositionMessage(mouseX, mouseY)
    }
}