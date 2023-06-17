package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.EventCameraPositionMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class EventCameraPositionHandler : MessageHandler<EventCameraPositionMessage> {

    override fun handle(client: Client, world: World, message: EventCameraPositionMessage) {
        client.cameraPitch = message.pitch
        client.cameraYaw = message.yaw
    }
}