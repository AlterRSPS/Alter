package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.npcs.OpNpcT
import org.alter.game.message.MessageHandler
import org.alter.game.model.attr.*
import org.alter.game.model.entity.Client
import org.alter.game.model.entity.Entity
import java.lang.ref.WeakReference

@Suppress("UNREACHABLE_CODE")
class OpNpcTHandler : MessageHandler<OpNpcT> {
    override fun consume(
        client: Client,
        message: OpNpcT,
    ) {
        val npc = client.world.npcs[message.index] ?: return
        val parent = message.selectedInterfaceId
        val child = message.selectedComponentId

        if (!client.lock.canNpcInteract()) {
            return
        }

        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()
        println("$client, ${npc.id} ${message.index} $parent $child ${message.controlKey}")
        val verify = message.selectedObj
        /**
         * @TODO Need to validate this, currently working on path / movement.
         */
        client.attr[INTERACTING_NPC_ATTR] = WeakReference(npc)
        client.attr[INTERACTING_COMPONENT_PARENT] = parent
        client.attr[INTERACTING_COMPONENT_CHILD] = child

        /**
         * Switch case between interface ids.
         * On item use npc does not imediatly turn to player.
         *
         */
        if (child == 0) {
            if (client.world.plugins.executeItemOnNpc(client, npc.id, verify)
                || client.world.plugins.executeItemOnNpc(client, verify)) {
                return
            }
            if (client.world.devContext.debugItemActions) {
                client.writeMessage("Unhandled item on npc [ $verify on ${npc.id}] ] ")
            }
        } else {
            if (!client.world.plugins.executeSpellOnNpc(client, parent, child)) {
                client.writeMessage(Entity.NOTHING_INTERESTING_HAPPENS)
                if (client.world.devContext.debugMagicSpells) {
                    client.writeMessage("Unhandled magic spell: [$parent, $child] out here")
                }
            }
        }
    }
}
