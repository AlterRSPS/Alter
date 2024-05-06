package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.npcs.OpNpc
import org.alter.game.action.PawnPathAction
import org.alter.game.message.MessageHandler
import org.alter.game.model.World
import org.alter.game.model.attr.INTERACTING_NPC_ATTR
import org.alter.game.model.attr.INTERACTING_OPT_ATTR
import org.alter.game.model.entity.Client
import org.alter.game.model.priv.Privilege
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpcHandler : MessageHandler<OpNpc> {

    override fun handle(client: Client, world: World, message: OpNpc) {
        val npc = world.npcs[message.index] ?: return

        if (message.op == 2) {
            if (!client.lock.canAttack()) {
                return
            }
        } else {
            if (!client.lock.canNpcInteract()) {
                return
            }
        }

        log(client, "Npc option %d: index=%d, movement=%d, npc=%s", message.op, message.index, message.controlKey, npc)

        if (message.controlKey && world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.moveTo(world.findRandomTileAround(npc.tile, 1) ?: npc.tile)
        }

        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()

        if (message.op == 2) {
            client.attack(npc)
        } else {
            client.attr[INTERACTING_OPT_ATTR] = 1
            client.attr[INTERACTING_NPC_ATTR] = WeakReference(npc)
            client.executePlugin(PawnPathAction.walkPlugin)
        }
    }
}