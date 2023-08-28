package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.EventMouseClickMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventMouseClickHandler : MessageHandler<EventMouseClickMessage> {
    override fun handle(client: Client, world: World, message: EventMouseClickMessage) {
        // TODO
    }
}