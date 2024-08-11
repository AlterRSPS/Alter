package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.misc.user.Teleport
import org.alter.game.message.MessageHandler
import org.alter.game.model.entity.Client
import org.alter.game.model.priv.Privilege

/**
 * @author Tom <rspsmods@gmail.com>
 */
class TeleportHandler : MessageHandler<Teleport> {
    override fun consume(
        client: Client,
        message: Teleport,
    ) {
        if (!client.lock.canMove()) {
            return
        }

        log(client, "Teleport world map: unknown=%d, x=%d, z=%d, height=%d", message.oculusSyncValue, message.x, message.z, message.level)

        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()

        if (client.world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.moveTo(message.x, message.z, message.level)
        }
    }
}
