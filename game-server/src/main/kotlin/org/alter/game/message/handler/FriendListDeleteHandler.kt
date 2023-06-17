package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.FriendListDeleteMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

class FriendListDeleteHandler : MessageHandler<FriendListDeleteMessage> {
    override fun handle(client: Client, world: World, message: FriendListDeleteMessage) {
        client.social.deleteFriend(client, message.username)
    }
}