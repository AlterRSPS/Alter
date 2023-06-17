package org.alter.game.message.impl

import org.alter.game.message.Message

data class OpPlayerTMessage(val playerIndex: Int, val keydown: Boolean, val verify: Int, val spellChildIndex: Int, val componentHash: Int) : Message