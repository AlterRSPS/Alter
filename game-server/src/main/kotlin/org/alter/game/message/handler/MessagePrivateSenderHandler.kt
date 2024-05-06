package org.alter.game.message.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import net.rsprot.protocol.game.incoming.messaging.MessagePrivate
import org.alter.game.message.MessageHandler
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @TODO
 */
class MessagePrivateSenderHandler : MessageHandler<MessagePrivate> {
    override fun handle(client: Client, world: World, message: MessagePrivate) {
        logger.info { "Sender: ${client.username} - Target: ${message.name} - Message: ${message.message}" }
        val target = world.getPlayerForName(message.name)
        if (target != null) {
            logger.info { "Attempting to send packet to target" }
            client.social.sendPrivateMessage(client, target, message.message)
        }
    }
    companion object {
        private val logger = KotlinLogging.logger{}
    }
}