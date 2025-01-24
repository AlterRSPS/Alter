package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.npcs.OpNpc6
import org.alter.game.message.MessageHandler
import org.alter.game.model.ExamineEntityType
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpc6Handler : MessageHandler<OpNpc6> {
    override fun consume(
        client: Client,
        message: OpNpc6,
    ) {
        client.world.sendExamine(client, message.id, ExamineEntityType.NPC)
    }
}
