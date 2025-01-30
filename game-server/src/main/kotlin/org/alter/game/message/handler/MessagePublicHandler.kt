package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.messaging.MessagePublic
import org.alter.game.message.MessageHandler
import org.alter.game.model.entity.Client
import org.alter.game.service.log.LoggerService

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MessagePublicHandler : MessageHandler<MessagePublic> {
    override fun consume(
        client: Client,
        message: MessagePublic,
    ) {
        client.avatar.extendedInfo.setChat(
            message.colour,
            message.effect,
            client.privilege.icon,
            message.type == 1,
            message.message,
            pattern = message.pattern?.asByteArray(),
        )
        client.world.getService(LoggerService::class.java, searchSubclasses = true)?.logPublicChat(client, message.message)
    }
}
