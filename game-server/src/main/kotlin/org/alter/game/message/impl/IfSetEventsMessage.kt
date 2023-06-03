package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class IfSetEventsMessage(val hash: Int, val fromChild: Int, val toChild: Int, val setting: Int) : Message