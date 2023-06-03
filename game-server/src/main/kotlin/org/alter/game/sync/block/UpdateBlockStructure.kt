package org.alter.game.sync.block

import org.alter.game.message.MessageValue

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class UpdateBlockStructure(val bit: Int, val values: List<MessageValue>)