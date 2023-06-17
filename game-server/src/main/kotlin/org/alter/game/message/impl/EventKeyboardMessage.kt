package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class EventKeyboardMessage(val events: List<KeyEvent>) : Message {

    data class KeyEvent(val key: Int, val lastKeyPress: Int)
}