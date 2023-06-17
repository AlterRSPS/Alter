package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class ObjCountMessage(val item: Int, val oldAmount: Int, val newAmount: Int, val tile: Int) : Message