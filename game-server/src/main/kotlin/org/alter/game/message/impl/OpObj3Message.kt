package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class OpObj3Message(val item: Int, val x: Int, val z: Int, val movementType: Int) : Message