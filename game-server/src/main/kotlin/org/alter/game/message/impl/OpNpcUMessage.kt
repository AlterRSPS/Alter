package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class OpNpcUMessage(val componentHash: Int, val npcIndex: Int, val item: Int, val slot: Int, val movementType: Int) : Message