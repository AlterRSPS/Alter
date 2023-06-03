package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.EventAppletFocusMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventAppletFocusHandler : MessageHandler<EventAppletFocusMessage> {

    override fun handle(client: Client, world: World, message: EventAppletFocusMessage) {
        when (message.state) {
            FOCUSED_STATE -> client.appletFocused = true
            UNFOCUSED_STATE -> client.appletFocused = false
        }
    }

    companion object {
        private const val UNFOCUSED_STATE = 0
        private const val FOCUSED_STATE = 1
    }
}