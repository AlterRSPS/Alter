package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.resumed.ResumePNameDialog
import org.alter.game.message.MessageHandler
import org.alter.game.model.entity.Client
import org.alter.game.model.queue.QueueTask

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePNameDialogHandler : MessageHandler<ResumePNameDialog> {
    override fun consume(
        client: Client,
        message: ResumePNameDialog,
    ) {
        val name = message.name
        val target = client.world.getPlayerForName(name)

        log(client, "Player username input dialog: username=%s", name)

        client.queues.submitReturnValue(target ?: QueueTask.EMPTY_RETURN_VALUE)
    }
}
