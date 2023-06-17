package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class ResumePauseButtonMessage(val interfaceId: Int, val component: Int, val slot: Int) : Message