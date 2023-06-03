package org.alter.game.message.handler

import org.alter.game.action.PawnPathAction
import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.OpNpc3Message
import org.alter.game.model.World
import org.alter.game.model.attr.INTERACTING_NPC_ATTR
import org.alter.game.model.attr.INTERACTING_OPT_ATTR
import org.alter.game.model.entity.Client
import org.alter.game.model.priv.Privilege
import java.lang.ref.WeakReference

/**
 * @author Tom <rspsmods@gmail.com>
 */
class OpNpc3Handler : MessageHandler<OpNpc3Message> {

    override fun handle(client: Client, world: World, message: OpNpc3Message) {
        val npc = world.npcs[message.index] ?: return

        if (!client.lock.canNpcInteract()) {
            return
        }

        log(client, "Npc option 3: index=%d, movement=%d, npc=%s", message.index, message.movementType, npc)

        if (message.movementType == 1 && world.privileges.isEligible(client.privilege, Privilege.ADMIN_POWER)) {
            client.moveTo(world.findRandomTileAround(npc.tile, 1) ?: npc.tile)
        }

        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()

        client.attr[INTERACTING_OPT_ATTR] = 3
        client.attr[INTERACTING_NPC_ATTR] = WeakReference(npc)
        client.executePlugin(PawnPathAction.walkPlugin)
    }
}