package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class SynthSoundMessage(val sound: Int, val volume: Int, val delay: Int) : Message