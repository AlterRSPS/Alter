package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.ResumePauseButtonMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePauseButtonHandler : MessageHandler<ResumePauseButtonMessage> {

    override fun handle(client: Client, world: World, message: ResumePauseButtonMessage) {
        log(client, "Continue dialog: component=[%d:%d], slot=%d", message.interfaceId, message.component, message.slot)
        client.queues.submitReturnValue(message)
    }
}