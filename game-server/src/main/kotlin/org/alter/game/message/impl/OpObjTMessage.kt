package org.alter.game.message.impl

import org.alter.game.message.Message

data class OpObjTMessage(val x: Int, val z: Int, val item: Int, val slot: Int, val obj: Int, val movementType: Int, val hash: Int) : Message