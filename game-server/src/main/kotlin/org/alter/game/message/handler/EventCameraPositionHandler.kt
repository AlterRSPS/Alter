package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.events.EventCameraPosition
import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.EventCameraPositionMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventCameraPositionHandler : MessageHandler<EventCameraPosition> {

    override fun handle(client: Client, world: World, message: EventCameraPosition) {
        client.cameraPitch = message.angleX
        client.cameraYaw = message.angleY
    }
}