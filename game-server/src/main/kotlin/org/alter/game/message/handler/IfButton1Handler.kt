package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.IfButtonMessage
import org.alter.game.model.World
import org.alter.game.model.attr.*
import org.alter.game.model.entity.Client
/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfButton1Handler : MessageHandler<IfButtonMessage> {
    override fun handle(client: Client, world: World, message: IfButtonMessage) {

        val interfaceId = message.hash shr 16
        val component = message.hash and 0xFFFF
        val option = message.option
        if (!client.interfaces.isVisible(interfaceId)) {
            return
        }
        log(client, "Click button: component=[%d:%d], option=%d, slot=%d, item=%d", interfaceId, component, option, message.slot, message.item)

        client.attr[INTERACTING_OPT_ATTR] = option
        client.attr[INTERACTING_ITEM_ID] = message.item
        client.attr[INTERACTING_SLOT_ATTR] = message.slot

        if (world.plugins.executeButton(client, interfaceId, component)) {
            return
        }

        if (world.devContext.debugButtons) {
            client.writeMessage("Unhandled button action: [component=[$interfaceId:$component], option=$option, slot=${message.slot}, item=${message.item}]")
        }
    }
}