package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.ClanJoinChatLeaveChatMessage
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClanJoinChatLeaveHandler : MessageHandler<ClanJoinChatLeaveChatMessage> {

    override fun handle(client: Client, world: World, message: ClanJoinChatLeaveChatMessage) {
        throw RuntimeException("Unhandled.")
    }
}