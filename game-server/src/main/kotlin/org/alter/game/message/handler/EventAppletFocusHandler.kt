package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.events.EventAppletFocus
import org.alter.game.message.MessageHandler
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventAppletFocusHandler : MessageHandler<EventAppletFocus> {
    override fun consume(
        client: Client,
        message: EventAppletFocus,
    ) {
        client.appletFocused = message.inFocus
    }
}
