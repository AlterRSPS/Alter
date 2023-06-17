package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class ObjDelMessage(val item: Int, val tile: Int, val amount: Int) : Message