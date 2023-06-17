package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.OpLoc6Message
import org.alter.game.model.ExamineEntityType
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpLoc6Handler : MessageHandler<OpLoc6Message> {

    override fun handle(client: Client, world: World, message: OpLoc6Message) {
        world.sendExamine(client, message.id, ExamineEntityType.OBJECT)
    }
}