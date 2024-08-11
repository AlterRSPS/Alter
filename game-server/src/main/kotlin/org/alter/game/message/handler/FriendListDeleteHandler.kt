package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.social.FriendListDel
import org.alter.game.message.MessageHandler
import org.alter.game.model.entity.Client

class FriendListDeleteHandler : MessageHandler<FriendListDel> {
    override fun consume(
        client: Client,
        message: FriendListDel,
    ) {
        client.social.deleteFriend(client, message.name)
    }
}
