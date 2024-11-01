package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.resumed.ResumePObjDialog
import org.alter.game.message.MessageHandler
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePObjDialogHandler : MessageHandler<ResumePObjDialog> {
    override fun consume(
        client: Client,
        message: ResumePObjDialog,
    ) {
        log(client, "Searched item: item=%d", message.obj)
        client.queues.submitReturnValue(message.obj)
    }
}
