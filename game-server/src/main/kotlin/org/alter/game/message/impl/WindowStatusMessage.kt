package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class WindowStatusMessage(val mode: Int, val width: Int, val height: Int) : Message