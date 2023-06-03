package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class OpNpcTMessage(val npcIndex: Int, val componentHash: Int, val componentSlot: Int, val verify: Int, val movementType: Int) : Message