package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class IfMoveSubMessage(val from: Int, val to: Int) : Message