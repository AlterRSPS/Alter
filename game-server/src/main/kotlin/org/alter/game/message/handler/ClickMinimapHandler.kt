package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.misc.user.MoveMinimapClick
import org.alter.game.message.MessageHandler
import org.alter.game.model.attr.CLIENT_KEY_COMBINATION
import org.alter.game.model.move.MovementQueue
import org.alter.game.model.entity.Client
import org.alter.game.model.move.walkTo

class ClickMinimapHandler : MessageHandler<MoveMinimapClick> {
    override fun consume(
        client: Client,
        message: MoveMinimapClick,
    ) {
       log(client, "Click minimap: x=%d, y=%d, type=%d", message.x, message.z, message.keyCombination)
       client.attr[CLIENT_KEY_COMBINATION] = message.keyCombination
       client.walkTo(message.x, message.z, MovementQueue.StepType.NORMAL)
    }
}
