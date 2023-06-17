package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class EventCameraPositionMessage(val pitch: Int, val yaw: Int) : Message