package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.ResumePCountDialogMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePCountDialogHandler : MessageHandler<ResumePCountDialogMessage> {

    override fun handle(client: Client, world: World, message: ResumePCountDialogMessage) {
        log(client, "Integer input dialog: input=%d", message.input)
        client.queues.submitReturnValue(Math.max(0, message.input))
    }
}