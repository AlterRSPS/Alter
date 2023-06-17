package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.OpObj6Message
import org.alter.game.model.ExamineEntityType
import org.alter.game.model.World
import org.alter.game.model.entity.Client

class OpObj6Handler : MessageHandler<OpObj6Message> {

    override fun handle(client: Client, world: World, message: OpObj6Message) {
        world.sendExamine(client, message.item, ExamineEntityType.ITEM)
    }
}