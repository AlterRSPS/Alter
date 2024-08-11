package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.npcs.OpNpc
import org.alter.game.model.move.PawnPathAction
import org.alter.game.message.MessageHandler
import org.alter.game.model.attr.INTERACTING_NPC_ATTR
import org.alter.game.model.attr.INTERACTING_OPT_ATTR
import org.alter.game.model.entity.Client
import org.alter.game.model.move.PawnPathAction.combatPath
import java.lang.ref.WeakReference

class OpNpcHandler : MessageHandler<OpNpc> {
    override fun accept(
        client: Client,
        message: OpNpc,
    ) {
        val npc = client.world.npcs[message.index] ?: return
        log(client, "Npc option %d: index=%d, movement=%b, npc=%s", message.op, message.index, message.controlKey, npc)
        /**
         * @TODO
         */
        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()

        if (message.op == 2) {
            client.combatPath(npc)
            //client.attack(npc)
        } else {
            client.attr[INTERACTING_OPT_ATTR] = message.op
            client.attr[INTERACTING_NPC_ATTR] = WeakReference(npc)
            client.executePlugin(PawnPathAction.walkPlugin)
        }
    }
}
