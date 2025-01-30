package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.events.EventMouseClick
import org.alter.game.message.MessageHandler
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventMouseClickHandler : MessageHandler<EventMouseClick> {
    override fun consume(
        client: Client,
        message: EventMouseClick,
    ) {
        /**
         * TODO("Implement mouse event click")
         */
    }
}
