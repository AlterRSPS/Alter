package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.misc.client.WindowStatus
import org.alter.game.message.MessageHandler
import org.alter.game.model.World
import org.alter.game.model.attr.DISPLAY_MODE_CHANGE_ATTR
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class WindowStatusHandler : MessageHandler<WindowStatus> {

    override fun handle(client: Client, world: World, message: WindowStatus) {
        client.clientWidth = message.frameWidth
        client.clientHeight = message.frameHeight
        client.attr[DISPLAY_MODE_CHANGE_ATTR] = message.windowMode
        world.plugins.executeWindowStatus(client)
    }
}