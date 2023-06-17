package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.ClickWorldMapMessage
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.entity.Client
import org.alter.game.model.priv.Privilege

/**
 * @author HolyRSPS <dagreenrs@gmail.com>
 */
class ClickWorldMapHandler : MessageHandler<ClickWorldMapMessage> {

    override fun handle(client: Client, world: World, message: ClickWorldMapMessage) {
        if (world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            log(client, "Click world map: %s", Tile.from30BitHash(message.data).toString())
            client.moveTo(Tile.from30BitHash(message.data))
        }
    }
}