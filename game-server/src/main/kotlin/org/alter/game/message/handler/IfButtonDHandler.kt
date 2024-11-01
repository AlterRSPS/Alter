package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.buttons.IfButtonD
import org.alter.game.message.MessageHandler
import org.alter.game.model.attr.INTERACTING_COMPONENT_CHILD
import org.alter.game.model.attr.INTERACTING_ITEM_SLOT
import org.alter.game.model.attr.OTHER_ITEM_SLOT_ATTR
import org.alter.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfButtonDHandler : MessageHandler<IfButtonD> {
    override fun consume(
        client: Client,
        message: IfButtonD,
    ) {
        val fromSlot = message.selectedSub
        val fromItemId = message.selectedObj

        val toSlot = message.targetSub
        val toItemId = message.targetObj

        val fromInterfaceId = message.selectedInterfaceId
        val fromComponent = message.selectedComponentId
        val toInterfaceId = message.targetInterfaceId
        val toComponent = message.targetComponentId

        log(
            client,
            "Swap component to component item: src_component=[%d:%d], dst_component=[%d:%d], src_item=%d, dst_item=%d",
            fromInterfaceId,
            fromComponent,
            toInterfaceId,
            toComponent,
            fromItemId,
            toItemId,
        )

        client.attr[INTERACTING_ITEM_SLOT] = fromSlot
        client.attr[OTHER_ITEM_SLOT_ATTR] = toSlot
        client.attr[INTERACTING_COMPONENT_CHILD] = fromComponent

        val swapped =
            client.world.plugins.executeComponentToComponentItemSwap(
                client,
                fromInterfaceId,
                fromComponent,
                toInterfaceId,
                toComponent,
            )
        if (!swapped && client.world.devContext.debugButtons) {
            client.writeMessage(
                "[IfButtonDHandler] Unhandled component to component swap: [from_item=$fromItemId, to_item=$toItemId, from_slot=$fromSlot, to_slot=$toSlot, " +
                    "from_component=[$fromInterfaceId:$fromComponent], to_component=[$toInterfaceId:$toComponent]]",
            )
        }
    }
}
