package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class IfButtonMessage(val hash: Int, val option: Int, val slot: Int, val item: Int) : Message