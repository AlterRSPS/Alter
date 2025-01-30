package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.buttons.If1Button
import org.alter.game.message.MessageHandler
import org.alter.game.model.attr.INTERACTING_ITEM_ID
import org.alter.game.model.attr.INTERACTING_OPT_ATTR
import org.alter.game.model.attr.INTERACTING_SLOT_ATTR
import org.alter.game.model.entity.Client

class IfModelOp1Handler : MessageHandler<If1Button> {
    override fun consume(
        client: Client,
        message: If1Button,
    ) {
        val interfaceId = message.interfaceId
        val component = message.componentId
        val option = 0
        val item = -1
        val slot = -1
        if (!client.interfaces.isVisible(interfaceId)) {
            return
        }
        log(client, "Click button: component=[%d:%d], option=%d, slot=%d, item=%d", interfaceId, component, option, slot, item)

        client.attr[INTERACTING_OPT_ATTR] = option
        client.attr[INTERACTING_ITEM_ID] = item
        client.attr[INTERACTING_SLOT_ATTR] = slot

        if (client.world.plugins.executeButton(client, interfaceId, component)) {
            return
        }

        if (client.world.devContext.debugButtons) {
            client.writeMessage("Unhandled button action: [component=[$interfaceId:$component], option=$option, slot=$slot, item=$item]")
        }
    }
}
