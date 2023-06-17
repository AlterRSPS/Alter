package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.OpNpc6Message
import org.alter.game.model.ExamineEntityType
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpc6Handler : MessageHandler<OpNpc6Message> {

    override fun handle(client: Client, world: World, message: OpNpc6Message) {
        world.sendExamine(client, message.npcId, ExamineEntityType.NPC)
    }
}