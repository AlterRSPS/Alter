package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.misc.user.ClickWorldMap
import org.alter.game.message.MessageHandler
import org.alter.game.model.Tile
import org.alter.game.model.entity.Client
import org.alter.game.model.move.moveTo
import org.alter.game.model.priv.Privilege

/**
 * @author HolyRSPS <dagreenrs@gmail.com>
 */
class ClickWorldMapHandler : MessageHandler<ClickWorldMap> {
    override fun consume(
        client: Client,
        message: ClickWorldMap,
    ) {
        if (client.world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            val dest = Tile(message.x, message.z, message.level)
            log(client, "Click world map: %s", dest.toString())
            client.moveTo(dest)
        }
    }
}
