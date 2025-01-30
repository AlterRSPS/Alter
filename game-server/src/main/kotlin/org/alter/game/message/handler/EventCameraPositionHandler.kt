package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.events.EventCameraPosition
import org.alter.game.message.MessageHandler
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventCameraPositionHandler : MessageHandler<EventCameraPosition> {
    override fun consume(
        client: Client,
        message: EventCameraPosition,
    ) {
        client.cameraPitch = message.angleX
        client.cameraYaw = message.angleY
    }
}
