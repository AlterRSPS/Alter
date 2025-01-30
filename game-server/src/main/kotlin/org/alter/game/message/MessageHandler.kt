package org.alter.game.message

import io.github.oshai.kotlinlogging.KotlinLogging
import net.rsprot.protocol.message.IncomingGameMessage
import net.rsprot.protocol.message.codec.incoming.MessageConsumer
import org.alter.game.model.entity.Client
import org.alter.game.service.log.LoggerService
import java.util.function.BiConsumer

/**
 * A [MessageHandler] is responsible for executing [Message] logic on the
 * game-thread.
 *
 * @author Tom <rspsmods@gmail.com>
 */
interface MessageHandler<T : IncomingGameMessage> : MessageConsumer<Client, T> {
    /**
     * Handles the [message] on the game-thread with the ability to read and write
     * to the [Client].
     */
    override fun consume(
        client: Client,
        message: T,
    )

    /**
     * A default method to log the handlers.
     */
    fun log(
        client: Client,
        format: String,
        vararg args: Any,
    ) {
        if (client.logPackets) {
            val message = String.format(format, *args)
            client.writeMessage(message)
            val logService = client.world.getService(LoggerService::class.java, searchSubclasses = true)
            // println("Logger-message: [$message] , client: [$client]")
            if (logService != null) {
                logService.logPacket(client, message)
            } else {
                logger.trace { message }
            }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
