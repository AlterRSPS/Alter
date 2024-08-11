package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.misc.user.MoveGameClick
import net.rsprot.protocol.game.outgoing.misc.player.SetMapFlag
import org.alter.game.message.MessageHandler
import org.alter.game.model.move.MovementQueue
import org.alter.game.model.attr.NO_CLIP_ATTR
import org.alter.game.model.entity.Client
import org.alter.game.model.entity.Entity
import org.alter.game.model.move.walkTo
import org.alter.game.model.priv.Privilege
import org.alter.game.model.timer.STUN_TIMER

class ClickMapHandler : MessageHandler<MoveGameClick> {
    override fun accept(
        client: Client,
        message: MoveGameClick
    ) {
        log(client, "Click map: x=%d, z=%d, type=%d", message.x, message.z, message.keyCombination)
        client.walkTo(message.x, message.z, stepType = MovementQueue.StepType.NORMAL, detectCollision = true)
    }
}
