package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.misc.client.MapBuildComplete
import org.alter.game.message.MessageHandler
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MapBuildCompleteHandler : MessageHandler<MapBuildComplete> {

    override fun handle(client: Client, world: World, message: MapBuildComplete) {
        client.lastMapBuildTime = world.currentCycle
    }
}