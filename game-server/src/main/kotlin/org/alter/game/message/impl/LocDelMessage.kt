package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class LocDelMessage(val settings: Int, val tile: Int) : Message