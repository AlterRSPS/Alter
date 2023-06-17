package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.IgnoreListAddMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

class IgnoreListAddHandler : MessageHandler<IgnoreListAddMessage> {
    override fun handle(client: Client, world: World, message: IgnoreListAddMessage) {
        client.social.addIgnore(client, message.username)
    }
}