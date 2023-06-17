package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class ObjAddMessage(val item: Int, val amount: Int, val tile: Int) : Message