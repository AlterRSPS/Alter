package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.MessagePublicMessage
import org.alter.game.model.ChatMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client
import org.alter.game.service.log.LoggerService
import org.alter.game.sync.block.UpdateBlockType
import java.io.IOException

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MessagePublicHandler : MessageHandler<MessagePublicMessage> {

    override fun handle(client: Client, world: World, message: MessagePublicMessage) {

        val decompressed = ByteArray(230)
        val huffman = world.huffman
        huffman.decompress(message.data, decompressed, message.length)

        val unpacked = String(decompressed, 0, message.length)
        val type = ChatMessage.ChatType.values.firstOrNull { it.id == message.type } ?: ChatMessage.ChatType.NONE
        val effect = ChatMessage.ChatEffect.values.firstOrNull { it.id == message.effect } ?: ChatMessage.ChatEffect.NONE
        val color = ChatMessage.ChatColor.values.firstOrNull { it.id == message.color } ?: ChatMessage.ChatColor.NONE

        client.blockBuffer.publicChat = ChatMessage(unpacked, client.privilege.icon, type, effect, color)

        client.addBlock(UpdateBlockType.PUBLIC_CHAT)

        world.getService(LoggerService::class.java, searchSubclasses = true)?.logPublicChat(client, unpacked)
    }
}