package org.alter.game.message.handler

import org.alter.game.action.PawnPathAction
import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.OpNpcUMessage
import org.alter.game.model.World
import org.alter.game.model.attr.INTERACTING_ITEM
import org.alter.game.model.attr.INTERACTING_ITEM_ID
import org.alter.game.model.attr.INTERACTING_ITEM_SLOT
import org.alter.game.model.attr.INTERACTING_NPC_ATTR
import org.alter.game.model.entity.Client
import org.alter.game.model.priv.Privilege
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpcUHandler : MessageHandler<OpNpcUMessage> {

    override fun handle(client: Client, world: World, message: OpNpcUMessage) {
        val index = message.npcIndex
        val npc = world.npcs[index] ?: return

        if (!client.lock.canNpcInteract() || !client.lock.canItemInteract()) {
            return
        }

        val movementType = message.movementType
        val itemId = message.item
        val itemSlot = message.slot

        val item = client.inventory[itemSlot] ?: return

        if (item.id != itemId) {
            return
        }

        log(client, "Item on npc: movement=%d, item=%s, slot=%d, npc=%s, index=%d", movementType, item, itemSlot, npc, index)

        if (movementType == 1 && world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.moveTo(world.findRandomTileAround(npc.tile, 1) ?: npc.tile)
        }

        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()

        client.attr[INTERACTING_NPC_ATTR] = WeakReference(npc)
        client.attr[INTERACTING_ITEM] = WeakReference(item)
        client.attr[INTERACTING_ITEM_ID] = item.id
        client.attr[INTERACTING_ITEM_SLOT] = itemSlot
        client.executePlugin(PawnPathAction.itemUsePlugin)
    }
}