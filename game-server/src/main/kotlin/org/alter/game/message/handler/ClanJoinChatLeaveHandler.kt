package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.friendchat.FriendChatJoinLeave
import org.alter.game.message.MessageHandler
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClanJoinChatLeaveHandler : MessageHandler<FriendChatJoinLeave> {
    override fun consume(
        client: Client,
        message: FriendChatJoinLeave,
    ) {
        throw RuntimeException("Unhandled.")
    }
}
