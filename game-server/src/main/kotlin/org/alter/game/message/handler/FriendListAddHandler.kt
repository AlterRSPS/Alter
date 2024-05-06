package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.social.FriendListAdd
import org.alter.game.message.MessageHandler
import org.alter.game.model.World
import org.alter.game.model.entity.Client

class FriendListAddHandler : MessageHandler<FriendListAdd> {
    override fun handle(client: Client, world: World, message: FriendListAdd) {
        client.social.addFriend(client, message.name)
    }
}