package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.misc.client.DetectModifiedClient
import org.alter.game.message.MessageHandler
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DetectModifiedClientHandler : MessageHandler<DetectModifiedClient> {
    override fun consume(
        client: Client,
        message: DetectModifiedClient,
    ) {
        log(client, "Detected modified client for player %s.", client.username)
    }
}
