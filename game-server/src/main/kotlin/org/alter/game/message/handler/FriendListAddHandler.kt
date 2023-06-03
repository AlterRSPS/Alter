package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.FriendListAddMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

class FriendListAddHandler : MessageHandler<FriendListAddMessage> {
    override fun handle(client: Client, world: World, message: FriendListAddMessage) {
        client.social.addFriend(client, message.username)
    }
}