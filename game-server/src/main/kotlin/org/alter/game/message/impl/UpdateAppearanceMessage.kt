package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateAppearanceMessage(val gender: Int, val appearance: IntArray, val colors: IntArray) : Message