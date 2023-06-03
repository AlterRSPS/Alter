package org.alter.game.message.impl

import org.alter.game.message.Message

data class MessagePrivateReceiverMessage(val target: String, val unknown: Int, val unknown2: Int, val rights: Int, val message: String) : Message