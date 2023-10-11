package org.alter.game.message

import org.alter.game.model.World
import org.alter.game.model.entity.Client
import org.alter.game.service.log.LoggerService

import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * A [MessageHandler] is responsible for executing [Message] logic on the
 * game-thread.
 *
 * @author Tom <rspsmods@gmail.com>
 */
interface MessageHandler<T : Message> {

    /**
     * Handles the [message] on the game-thread with the ability to read and write
     * to the [Client].
     */
    fun handle(client: Client, world: World, message: T)

    /**
     * A default method to log the handlers.
     */
    fun log(client: Client, format: String, vararg args: Any) {
        if (client.logPackets) {
            val message = String.format(format, *args)
            val logService = client.world.getService(LoggerService::class.java, searchSubclasses = true)
            //println("Logger-message: [$message] , client: [$client]")
            if (logService != null) {
                logService.logPacket(client, message)
            } else {
                logger.trace(message)
            }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger{}
    }
}