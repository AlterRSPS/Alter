package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.resumed.ResumePauseButton
import org.alter.game.message.MessageHandler
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePauseButtonHandler : MessageHandler<ResumePauseButton> {

    override fun handle(client: Client, world: World, message: ResumePauseButton) {
        log(client, "Continue dialog: component=[%d:%d], slot=%d", message.interfaceId, message.componentId, message.sub)
        client.queues.submitReturnValue(message)
    }
}