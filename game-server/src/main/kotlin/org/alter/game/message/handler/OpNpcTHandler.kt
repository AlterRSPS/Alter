package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.npcs.OpNpcT
import org.alter.game.message.MessageHandler
import org.alter.game.model.attr.*
import org.alter.game.model.entity.Client
import org.alter.game.model.entity.Entity
import org.alter.game.model.move.moveTo
import org.alter.game.model.priv.Privilege
import java.lang.ref.WeakReference

@Suppress("UNREACHABLE_CODE")
class OpNpcTHandler : MessageHandler<OpNpcT> {
    override fun accept(
        client: Client,
        message: OpNpcT,
    ) {
        val npc = client.world.npcs[message.index] ?: return
        val parent = message.selectedInterfaceId
        val child = message.selectedComponentId

        TODO("Fix this shit. OpNpcTHandler.")
        return

        if (!client.lock.canNpcInteract()) {
            return
        }

        log(
            client,
            "Spell/Item on npc: npc=%d. index=%d, component=[%d:%d], movement=%d",
            npc.id,
            message.index,
            parent,
            child,
            message.controlKey,
        )

        client.interruptQueues()
        client.resetInteractions()

        if (message.controlKey && client.world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.moveTo(client.world.findRandomTileAround(npc.tile, 1) ?: npc.tile)
        }
        //client.walkTo(client.world.findRandomTileAround(npc.tile, 1) ?: npc.tile)

        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()
        val verify = message.selectedObj

        client.attr[INTERACTING_NPC_ATTR] = WeakReference(npc)
        client.attr[INTERACTING_COMPONENT_PARENT] = parent
        client.attr[INTERACTING_COMPONENT_CHILD] = child

        if (child == 0) {
            if (!client.world.plugins.executeItemOnNpc(client, npc.id, verify)) {
                if (client.world.devContext.debugItemActions) {
                    client.writeMessage("Unhandled item on npc [ $verify on ${npc.id}] ] ")
                }
            } else if (!client.world.plugins.executeItemOnNpc(client, verify)) {
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
    }
}
