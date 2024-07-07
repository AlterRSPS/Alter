package org.alter.game.message

import io.github.oshai.kotlinlogging.KotlinLogging
import org.alter.game.model.entity.Client

/**
 * @TODO Add checks if we are allowed to unregister the players entity.
 */
class DisconnectionHook(var client: Client) : Runnable {
    override fun run() {
        logger.info { "Channel disconnected ${client.username}" }
        client.world.unregister(client.world.getPlayerForUid(client.uid)!!)
        client.channelFlush()
    }

    companion object {
        private val logger = KotlinLogging.logger {}

    }
}