package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.MessagePrivateSenderMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client
import mu.KLogging

class MessagePrivateSenderHandler : MessageHandler<MessagePrivateSenderMessage> {

    override fun handle(client: Client, world: World, message: MessagePrivateSenderMessage) {
        val decompressed = ByteArray(230)
        val huffman = world.huffman
        huffman.decompress(message.message, decompressed, message.length)
        val unpacked = String(decompressed, 0, message.length)

        logger.info { "Sender: ${client.username} - Target: ${message.target} - Message: [${message.length}] $unpacked" }

        val target = world.getPlayerForName(message.target)
        if (target != null) {
            logger.info { "Attempting to send packet to target" }
            client.social.sendPrivateMessage(client, target, message.length, message.message)
        }
    }

    companion object: KLogging()

}