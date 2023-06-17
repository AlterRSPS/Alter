package org.alter.game.message.impl

import org.alter.game.message.Message

data class CamShakeMessage(val index: Int, val left: Int, val center: Int, val right: Int) : Message
