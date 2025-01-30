package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.misc.user.CloseModal
import org.alter.game.message.MessageHandler
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CloseMainComponentHandler : MessageHandler<CloseModal> {
    override fun consume(
        client: Client,
        message: CloseModal,
    ) {
        client.closeInterfaceModal()
    }
}
