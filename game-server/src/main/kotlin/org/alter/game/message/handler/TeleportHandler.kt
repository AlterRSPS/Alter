package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.TeleportMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client
import org.alter.game.model.priv.Privilege

/**
 * @author Tom <rspsmods@gmail.com>
 */
class TeleportHandler : MessageHandler<TeleportMessage> {

    override fun handle(client: Client, world: World, message: TeleportMessage) {
        if (!client.lock.canMove()) {
            return
        }

        log(client, "Teleport world map: unknown=%d, x=%d, z=%d, height=%d", message.unknown, message.x, message.z, message.height)

        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()

        if (world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.moveTo(message.x, message.z, message.height)
        }
    }
}