package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.CloseModalMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CloseMainComponentHandler : MessageHandler<CloseModalMessage> {

    override fun handle(client: Client, world: World, message: CloseModalMessage) {
        client.closeInterfaceModal()
    }
}