package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class IfSetNpcHeadMessage(val hash: Int, val npc: Int) : Message