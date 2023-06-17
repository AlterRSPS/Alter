package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class IfSetObjectMessage(val hash: Int, val item: Int, val amount: Int) : Message