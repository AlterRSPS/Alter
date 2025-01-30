package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.players.OpPlayerT
import org.alter.game.message.MessageHandler
import org.alter.game.model.attr.INTERACTING_COMPONENT_CHILD
import org.alter.game.model.attr.INTERACTING_COMPONENT_PARENT
import org.alter.game.model.attr.INTERACTING_PLAYER_ATTR
import org.alter.game.model.entity.Client
import org.alter.game.model.entity.Entity
import java.lang.ref.WeakReference

class OpPlayerTHandler : MessageHandler<OpPlayerT> {
    override fun consume(
        client: Client,
        message: OpPlayerT,
    ) {
        val player = client.world.players[message.index] ?: return
        val parent = message.selectedInterfaceId
        val child = message.selectedComponentId

        if (!client.lock.canPlayerInteract()) {
            return
        }

        log(client, "Spell on player: player=%s. index=%d, component=[%d:%d]", player.username, message.index, parent, child)

        client.interruptQueues()
        client.resetInteractions()
        client.closeInterfaceModal()

        client.attr[INTERACTING_PLAYER_ATTR] = WeakReference(player)
        client.attr[INTERACTING_COMPONENT_PARENT] = parent
        client.attr[INTERACTING_COMPONENT_CHILD] = child

        // TODO: add verification of active spellbook add in 196
        if (!client.world.plugins.executeSpellOnPlayer(client, parent, child)) {
            client.writeMessage(Entity.NOTHING_INTERESTING_HAPPENS)
            if (client.world.devContext.debugMagicSpells) {
                client.writeMessage("Unhandled magic spell: [$parent, $child]")
            }
        }
    }
}
