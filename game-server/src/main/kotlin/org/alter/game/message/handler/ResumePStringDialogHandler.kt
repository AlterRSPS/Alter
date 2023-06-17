package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.ResumePStringDialogMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePStringDialogHandler : MessageHandler<ResumePStringDialogMessage> {

    override fun handle(client: Client, world: World, message: ResumePStringDialogMessage) {
        log(client, "String input dialog: input=%s", message.input)
        client.queues.submitReturnValue(message.input)
    }
}