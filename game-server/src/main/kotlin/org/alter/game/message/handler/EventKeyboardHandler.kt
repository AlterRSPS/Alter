package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.events.EventKeyboard
import org.alter.game.message.MessageHandler
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventKeyboardHandler : MessageHandler<EventKeyboard> {
    override fun consume(
        client: Client,
        message: EventKeyboard,
    ) {
    }
}
