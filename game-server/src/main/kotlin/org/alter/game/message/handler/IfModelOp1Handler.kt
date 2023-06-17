package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.IfButtonMessage
import org.alter.game.message.impl.IfModelOp1Message
import org.alter.game.model.World
import org.alter.game.model.entity.Client

class IfModelOp1Handler : MessageHandler<IfModelOp1Message> {

    override fun handle(client: Client, world: World, message: IfModelOp1Message) {
        IfButton1Handler().handle(client, world, IfButtonMessage(message.component, 0, -1, -1))
    }
}