package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.DetectModifiedClientMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DetectModifiedClientHandler : MessageHandler<DetectModifiedClientMessage> {

    override fun handle(client: Client, world: World, message: DetectModifiedClientMessage) {
        log(client, "Detected modified client for player %s (%s).", client.username, client.channel)
    }
}