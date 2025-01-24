package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.social.IgnoreListDel
import org.alter.game.message.MessageHandler
import org.alter.game.model.entity.Client

class IgnoreListDeleteHandler : MessageHandler<IgnoreListDel> {
    override fun consume(
        client: Client,
        message: IgnoreListDel,
    ) {
        client.social.deleteIgnore(client, message.name)
    }
}
