package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.EventKeyboardMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventKeyboardHandler : MessageHandler<EventKeyboardMessage> {

    override fun handle(client: Client, world: World, message: EventKeyboardMessage) {
    }
}