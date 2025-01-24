package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.buttons.IfButtonT
import org.alter.game.message.MessageHandler
import org.alter.game.model.attr.*
import org.alter.game.model.entity.Client
import java.lang.ref.WeakReference

class IfButtonTHandler : MessageHandler<IfButtonT> {
    override fun consume(
        client: Client,
        message: IfButtonT,
    ) {
        val fromInterfaceId = message.selectedInterfaceId
        val fromComponent = message.selectedComponentId
        val fromSlot = message.selectedSub
        val fromItemId = message.selectedObj

        val toInterfaceId = message.targetInterfaceId
        val toComponent = message.targetComponentId
        val toSlot = message.targetSub
        val toItemId = message.targetObj

        /**
         * @TODO
         * So switch by component 149 (Parent) Will be for Inventory.
         * SpellBook 161 (Parent)
         *
         * So we can switch by Parent and determine if it's item on item or spell on item
         */

        val fromItem = client.inventory[fromSlot] ?: return
        val toItem = client.inventory[toSlot] ?: return

        if (fromItem.id != fromItemId || toItem.id != toItemId) {
            return
        }

        if (!client.lock.canItemInteract()) {
            return
        }

        log(
            client,
            "ButtonT: from_component=[%d,%d], to_component=[%d,%d], from_item=%d, from_slot=%d, to_item=%d, to_slot=%d",
            fromInterfaceId,
            fromComponent,
            toInterfaceId,
            toComponent,
            fromItem.id,
            fromSlot,
            toItem.id,
            toSlot,
        )

        client.attr[INTERACTING_ITEM] = WeakReference(fromItem)
        client.attr[INTERACTING_ITEM_ID] = fromItem.id
        client.attr[INTERACTING_ITEM_SLOT] = fromSlot

        client.attr[OTHER_ITEM_ATTR] = WeakReference(toItem)
        client.attr[OTHER_ITEM_ID_ATTR] = toItem.id
        client.attr[OTHER_ITEM_SLOT_ATTR] = toSlot

        /**
         * @TODO Add support for (Any) Item on item <-- Example: Banker's note
         */
        var handled = client.world.plugins.executeItemOnItem(client, fromItem.id, toItem.id)

        /**
         * simple catchall registration to allow customizable fallback
         * for all other [on_item_on_item] interactions for a given [Item]
         * not explicitly registered
         *   Note| should be used with prejudice or for flavour
         */
        if (!handled) {
            handled = client.world.plugins.executeItemOnItem(client, fromItem.id, -1)
            if (handled && client.world.devContext.debugItemActions) {
                client.writeMessage(
                    "Unhandled item on item: [from_item=${fromItem.id}, to_item=${toItem.id}, from_slot=$fromSlot, to_slot=$toSlot, " +
                        "from_component=[$fromInterfaceId:$fromComponent], to_component=[$toInterfaceId:$toComponent]]",
                )
            }
        }

        if (!handled && client.world.devContext.debugItemActions) {
            client.writeMessage(
                "Unhandled item on item: [from_item=${fromItem.id}, to_item=${toItem.id}, from_slot=$fromSlot, to_slot=$toSlot, " +
                    "from_component=[$fromInterfaceId:$fromComponent], to_component=[$toInterfaceId:$toComponent]]",
            )
        }
    }
}
