package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.npcs.OpNpc6
import org.alter.game.message.MessageHandler
import org.alter.game.model.ExamineEntityType
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpc6Handler : MessageHandler<OpNpc6> {

    override fun handle(client: Client, world: World, message: OpNpc6) {
        world.sendExamine(client, message.id, ExamineEntityType.NPC)
    }
}