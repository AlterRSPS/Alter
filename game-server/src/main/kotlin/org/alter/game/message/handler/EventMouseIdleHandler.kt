package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.misc.client.Idle
import org.alter.game.message.MessageHandler
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventMouseIdleHandler : MessageHandler<Idle> {
    override fun consume(
        client: Client,
        message: Idle,
    ) {
    }
}
