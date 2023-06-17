package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class UpdateStatMessage(val skill: Int, val level: Int, val xp: Int) : Message