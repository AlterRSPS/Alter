package org.alter.game.message

import io.github.oshai.kotlinlogging.KotlinLogging
import org.alter.game.model.entity.Client

/**
 * @TODO Add checks if we are allowed to unregister the players entity.
 */
class DisconnectionHook(var client: Client) : Runnable {
    override fun run() {
        val player = client.world.getPlayerForUid(client.uid) ?: return
        if (!player.getPendingLogout()) {
            logger.info { "Channel `${client.username}` disconnected " }
            client.world.unregister(player)
            client.channelFlush()
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}

    }
}