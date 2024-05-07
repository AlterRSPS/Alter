package org.alter.game.message.handler

import net.rsprot.protocol.game.incoming.misc.user.UpdatePlayerModel
import org.alter.game.message.MessageHandler
import org.alter.game.model.appearance.Appearance
import org.alter.game.model.appearance.Gender
import org.alter.game.model.entity.Client
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateAppearanceHandler : MessageHandler<UpdatePlayerModel> {

    override fun accept(client: Client, message: UpdatePlayerModel) {
        val gender = if (message.bodyType == 1) Gender.FEMALE else Gender.MALE
        //this is retarded, doing it to minimize changes
        val looks = message.getColoursByteArray().map { it.toInt() and 0xFF }.toIntArray()
        val colors = message.getColoursByteArray().map { it.toInt() and 0xFF }.toIntArray()

        log(client, "Update appearance: gender=%s, appearance=%s, colors=%s", gender.toString(), Arrays.toString(looks), Arrays.toString(colors))
        client.queues.submitReturnValue(Appearance(looks, colors, gender))
    }
}