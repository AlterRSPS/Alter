package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.MapBuildCompleteMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MapBuildCompleteHandler : MessageHandler<MapBuildCompleteMessage> {

    override fun handle(client: Client, world: World, message: MapBuildCompleteMessage) {
        client.lastMapBuildTime = world.currentCycle
    }
}