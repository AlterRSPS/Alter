package org.alter.game.message.impl

import org.alter.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfOpenSubMessage(val parent: Int, val child: Int, val component: Int, val type: Int) : Message