package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.buttons.If3Button
import org.alter.game.message.MessageHandler
import org.alter.game.model.attr.INTERACTING_ITEM_ID
import org.alter.game.model.attr.INTERACTING_OPT_ATTR
import org.alter.game.model.attr.INTERACTING_SLOT_ATTR
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfButton1Handler : MessageHandler<If3Button> {
    override fun consume(
        client: Client,
        message: If3Button,
    ) {
        val interfaceId = message.interfaceId
        val component = message.componentId
        val option = message.op
        if (!client.interfaces.isVisible(interfaceId)) {
            return
        }
        log(
            client,
            "Click button: component=[%d:%d], option=%d, slot=%d, item=%d",
            interfaceId,
            component,
            option,
            message.sub,
            message.obj,
        )

        client.attr[INTERACTING_OPT_ATTR] = option
        client.attr[INTERACTING_ITEM_ID] = message.obj
        client.attr[INTERACTING_SLOT_ATTR] = message.sub

        if (client.world.plugins.executeButton(client, interfaceId, component)) {
            return
        }

        if (client.world.devContext.debugButtons) {
            client.writeMessage(
                "Unhandled button action: [component=[$interfaceId:$component], option=$option, slot=${message.sub}, item=${message.obj}]",
            )
        }
    }
}
