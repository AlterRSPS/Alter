package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.misc.user.MoveGameClick
import org.alter.game.message.MessageHandler
import org.alter.game.model.move.MovementQueue
import org.alter.game.model.entity.Client
import org.alter.game.model.move.walkTo

class ClickMapHandler : MessageHandler<MoveGameClick> {
    override fun accept(
        client: Client,
        message: MoveGameClick
    ) {
        log(client, "Click map: x=%d, z=%d, type=%d", message.x, message.z, message.keyCombination)
        client.walkTo(message.x, message.z, stepType = MovementQueue.StepType.NORMAL, detectCollision = true)
    }
}
