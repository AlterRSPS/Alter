package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.IgnoreListDeleteMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

class IgnoreListDeleteHandler : MessageHandler<IgnoreListDeleteMessage> {
    override fun handle(client: Client, world: World, message: IgnoreListDeleteMessage) {
        client.social.deleteIgnore(client, message.username)
    }
}