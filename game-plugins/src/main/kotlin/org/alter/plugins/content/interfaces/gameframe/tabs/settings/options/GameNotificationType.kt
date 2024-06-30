package org.alter.plugins.content.interfaces.options

import org.alter.game.model.bits.StorageBits

enum class GameNotificationType(override val startBit: Int, override val endBit: Int = startBit) : StorageBits {
    DISABLE_YELL(startBit = 0),
}
