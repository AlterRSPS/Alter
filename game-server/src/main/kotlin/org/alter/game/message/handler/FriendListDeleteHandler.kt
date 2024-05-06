package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.social.FriendListDel
import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.FriendListDeleteMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

class FriendListDeleteHandler : MessageHandler<FriendListDel> {
    override fun handle(client: Client, world: World, message: FriendListDel) {
        client.social.deleteFriend(client, message.name)
    }
}