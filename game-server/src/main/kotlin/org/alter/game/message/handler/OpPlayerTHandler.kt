package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.OpPlayerTMessage
import org.alter.game.model.World
import org.alter.game.model.attr.INTERACTING_COMPONENT_CHILD
import org.alter.game.model.attr.INTERACTING_COMPONENT_PARENT
import org.alter.game.model.attr.INTERACTING_PLAYER_ATTR
import org.alter.game.model.entity.Client
import org.alter.game.model.entity.Entity
import java.lang.ref.WeakReference

class OpPlayerTHandler : MessageHandler<OpPlayerTMessage> {

    override fun handle(client: Client, world: World, message: OpPlayerTMessage) {
        val player = world.players[message.playerIndex] ?: return
        val parent = message.componentHash shr 16
        val child = message.componentHash and 0xFFFF

        if (!client.lock.canPlayerInteract()) {
            return;
        }

        log(client, "Spell on player: player=%s. index=%d, component=[%d:%d]", player.username, message.playerIndex, parent, child)

        client.interruptQueues()
        client.resetInteractions()
        client.closeInterfaceModal()

        client.attr[INTERACTING_PLAYER_ATTR] = WeakReference(player)
        client.attr[INTERACTING_COMPONENT_PARENT] = parent
        client.attr[INTERACTING_COMPONENT_CHILD] = child

        // TODO: add verification of active spellbook add in 196
        if (!world.plugins.executeSpellOnPlayer(client, parent, child)) {
            client.writeMessage(Entity.NOTHING_INTERESTING_HAPPENS)
            if (world.devContext.debugMagicSpells) {
                client.writeMessage("Unhandled magic spell: [$parent, $child]")
            }
        }
    }
}