package org.alter.game.message.handler

import org.alter.game.action.PawnPathAction
import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.OpPlayer8Message
import org.alter.game.model.World
import org.alter.game.model.attr.INTERACTING_OPT_ATTR
import org.alter.game.model.attr.INTERACTING_PLAYER_ATTR
import org.alter.game.model.entity.Client
import java.lang.ref.WeakReference

/**
 * @author Triston Plummer ("Dread")
 */
class OpPlayer8Handler : MessageHandler<OpPlayer8Message> {

    override fun handle(client: Client, world: World, message: OpPlayer8Message) {
        val index = message.index
        // The interaction option id.
        val option = 8
        // The index of the option in the player's option array.
        val optionIndex = option - 1

        if (!client.lock.canPlayerInteract()) {
            return
        }

        val other = world.players[index] ?: return

        if (client.options[optionIndex] == null || other == client) {
            return
        }

        log(client, "Player option: name=%s, opt=%d", other.username, option)

        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()

        client.attr[INTERACTING_PLAYER_ATTR] = WeakReference(other)
        client.attr[INTERACTING_OPT_ATTR] = option
        client.executePlugin(PawnPathAction.walkPlugin)
    }
}