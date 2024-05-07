package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.npcs.OpNpcT
import org.alter.game.action.PawnPathAction
import org.alter.game.message.MessageHandler
import org.alter.game.model.attr.*
import org.alter.game.model.entity.Client
import org.alter.game.model.entity.Entity
import org.alter.game.model.priv.Privilege
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpcTHandler : MessageHandler<OpNpcT> {

    override fun accept(client: Client, message: OpNpcT) {
        if(true) {
            val npc = client.world.npcs[message.index] ?: return
            val parent = message.selectedInterfaceId
            val child = message.selectedComponentId

            if (!client.lock.canNpcInteract()) {
                return
            }

            log(client, "Spell/Item on npc: npc=%d. index=%d, component=[%d:%d], movement=%d", npc.id, message.index, parent, child, message.controlKey)

            client.interruptQueues()
            client.resetInteractions()

            if (message.controlKey && client.world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
                client.moveTo(client.world.findRandomTileAround(npc.tile, 1) ?: npc.tile)
            }
            client.walkTo(client.world.findRandomTileAround(npc.tile, 1) ?: npc.tile)
            client.closeInterfaceModal()
            client.interruptQueues()
            client.resetInteractions()
            val verify = message.selectedObj

            client.attr[INTERACTING_NPC_ATTR] = WeakReference(npc)
            client.attr[INTERACTING_COMPONENT_PARENT] = parent
            client.attr[INTERACTING_COMPONENT_CHILD] = child


            /**
             * @TODO
             * 1) Need to fix path
             * 2) Switch between Parent interface, so we will actually need to seperate the API from game since we will
             * use it's interface destinations for these cases.
             */
            if (child == 0) {
                if (!client.world.plugins.executeItemOnNpc(client, npc.id, verify)) {
                    if (client.world.devContext.debugItemActions) {
                        client.writeMessage("Unhandled item on npc [ $verify on ${npc.id}] ] ")
                    }
                } else if (!client.world.plugins.executeItemOnNpc(client, verify)){
                    client.writeMessage("Nothing interesting happens.")
                }
            } else {
                if (!client.world.plugins.executeSpellOnNpc(client, parent, child)) {
                    client.writeMessage(Entity.NOTHING_INTERESTING_HAPPENS)
                    if (client.world.devContext.debugMagicSpells) {
                        client.writeMessage("Unhandled magic spell: [$parent, $child] out here")
                    }
                }
            }
        } else {//Old Item on NPC OpNpcU
            val index = message.index
            val npc = client.world.npcs[index] ?: return

            if (!client.lock.canNpcInteract() || !client.lock.canItemInteract()) {
                return
            }

            val itemId = message.selectedObj//TODO IDK ADVO
            val itemSlot = message.selectedSub//TODO IDK ADVO

            val item = client.inventory[itemSlot] ?: return

            if (item.id != itemId) {
                return
            }

            log(client, "Item on npc: movement=%d, item=%s, slot=%d, npc=%s, index=%d", message.controlKey, item, itemSlot, npc, index)

            if (message.controlKey && client.world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
                client.moveTo(client.world.findRandomTileAround(npc.tile, 1) ?: npc.tile)
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
}