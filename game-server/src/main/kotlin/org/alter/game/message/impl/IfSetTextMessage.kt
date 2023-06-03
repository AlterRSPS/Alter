package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class IfSetTextMessage(val parent: Int, val child: Int, val text: String) : Message