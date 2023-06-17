package org.alter.game.message.impl

import org.alter.game.message.Message

data class IfButtonTMessage(val fromComponentHash: Int, val fromSlot: Int, val fromItem: Int,
                            val toComponentHash: Int, val toSlot: Int, val toItem: Int) : Message