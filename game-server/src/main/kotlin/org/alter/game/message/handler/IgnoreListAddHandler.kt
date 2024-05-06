package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.social.IgnoreListAdd
import org.alter.game.message.MessageHandler
import org.alter.game.model.World
import org.alter.game.model.entity.Client

class IgnoreListAddHandler : MessageHandler<IgnoreListAdd> {
    override fun handle(client: Client, world: World, message: IgnoreListAdd) {
        client.social.addIgnore(client, message.name)
    }
}