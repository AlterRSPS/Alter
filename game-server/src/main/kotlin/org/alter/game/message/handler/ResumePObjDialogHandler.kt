package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.ResumePObjDialogMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePObjDialogHandler : MessageHandler<ResumePObjDialogMessage> {

    override fun handle(client: Client, world: World, message: ResumePObjDialogMessage) {
        log(client, "Searched item: item=%d", message.item)
        client.queues.submitReturnValue(message.item)
    }
}