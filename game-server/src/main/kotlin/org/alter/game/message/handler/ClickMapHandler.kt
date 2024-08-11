package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.misc.user.MoveGameClick
import net.rsprot.protocol.game.outgoing.misc.player.SetMapFlag
import org.alter.game.message.MessageHandler
import org.alter.game.model.MovementQueue
import org.alter.game.model.attr.NO_CLIP_ATTR
import org.alter.game.model.entity.Client
import org.alter.game.model.entity.Entity
import org.alter.game.model.priv.Privilege
import org.alter.game.model.timer.STUN_TIMER

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ClickMapHandler : MessageHandler<MoveGameClick> {
    override fun consume(
        client: Client,
        message: MoveGameClick,
    ) {
        if (!client.lock.canMove()) {
            return
        }

        if (client.timers.has(STUN_TIMER)) {
            client.write(SetMapFlag(255, 255))
            client.writeMessage(Entity.YOURE_STUNNED)
            return
        }

        log(client, "Click map: x=%d, z=%d, type=%d", message.x, message.z, message.keyCombination)

        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()

        if (message.keyCombination == 2 && client.world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.moveTo(message.x, message.z, client.tile.height)
        } else {
            val stepType = if (message.keyCombination == 1) MovementQueue.StepType.FORCED_RUN else MovementQueue.StepType.NORMAL
            val noClip = client.attr[NO_CLIP_ATTR] ?: false
            client.walkTo(message.x, message.z, stepType, detectCollision = !noClip)
        }
    }
}
