package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.messaging.MessagePublic
import org.alter.game.message.MessageHandler
import org.alter.game.model.ChatMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client
import org.alter.game.service.log.LoggerService
import org.alter.game.sync.block.UpdateBlockType

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MessagePublicHandler : MessageHandler<MessagePublic> {

    override fun handle(client: Client, world: World, message: MessagePublic) {
        val type = ChatMessage.ChatType.values.firstOrNull { it.id == message.type } ?: ChatMessage.ChatType.NONE
        val effect = ChatMessage.ChatEffect.values.firstOrNull { it.id == message.effect } ?: ChatMessage.ChatEffect.NONE
        val color = ChatMessage.ChatColor.values.firstOrNull { it.id == message.colour } ?: ChatMessage.ChatColor.NONE

        client.blockBuffer.publicChat = ChatMessage(message.message, client.privilege.icon, type, effect, color)

        client.addBlock(UpdateBlockType.PUBLIC_CHAT)

        world.getService(LoggerService::class.java, searchSubclasses = true)?.logPublicChat(client, message.message)
    }
}