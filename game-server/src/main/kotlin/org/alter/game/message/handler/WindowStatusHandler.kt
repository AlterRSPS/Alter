package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.WindowStatusMessage
import org.alter.game.model.World
import org.alter.game.model.attr.DISPLAY_MODE_CHANGE_ATTR
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class WindowStatusHandler : MessageHandler<WindowStatusMessage> {

    override fun handle(client: Client, world: World, message: WindowStatusMessage) {
        client.clientWidth = message.width
        client.clientHeight = message.height
        client.attr[DISPLAY_MODE_CHANGE_ATTR] = message.mode
        world.plugins.executeWindowStatus(client)
    }
}