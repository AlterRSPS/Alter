package org.alter.game.message.impl

import org.alter.game.message.Message

data class IfSetModelMessage(val hash: Int, val model_id: Int) : Message
