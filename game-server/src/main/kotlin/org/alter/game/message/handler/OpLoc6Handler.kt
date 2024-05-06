package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.locs.OpLoc6
import org.alter.game.message.MessageHandler
import org.alter.game.model.ExamineEntityType
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpLoc6Handler : MessageHandler<OpLoc6> {

    override fun handle(client: Client, world: World, message: OpLoc6) {
        world.sendExamine(client, message.id, ExamineEntityType.OBJECT)
    }
}