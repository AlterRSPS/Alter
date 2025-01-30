package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.locs.OpLoc6
import org.alter.game.message.MessageHandler
import org.alter.game.model.ExamineEntityType
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpLoc6Handler : MessageHandler<OpLoc6> {
    override fun consume(
        client: Client,
        message: OpLoc6,
    ) {
        client.world.sendExamine(client, message.id, ExamineEntityType.OBJECT)
    }
}
