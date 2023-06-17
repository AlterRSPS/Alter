package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class OpObjUMessage(val componentHash: Int, val movementType: Int, val item: Int, val slot: Int,
                         val groundItem: Int, val x: Int, val z: Int) : Message