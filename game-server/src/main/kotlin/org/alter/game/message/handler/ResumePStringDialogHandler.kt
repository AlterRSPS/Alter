package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.resumed.ResumePStringDialog
import org.alter.game.message.MessageHandler
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePStringDialogHandler : MessageHandler<ResumePStringDialog> {
    override fun consume(
        client: Client,
        message: ResumePStringDialog,
    ) {
        log(client, "String input dialog: input=%s", message.string)
        client.queues.submitReturnValue(message.string)
    }
}
