package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.ClanJoinChatLeaveChatMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClanJoinChatLeaveHandler : MessageHandler<ClanJoinChatLeaveChatMessage> {
    override fun handle(client: Client, world: World, message: ClanJoinChatLeaveChatMessage) {
        throw RuntimeException("Unhandled.")
    }
}