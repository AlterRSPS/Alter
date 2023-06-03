package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class TeleportMessage(val unknown: Int, val x: Int, val z: Int, val height: Int) : Message