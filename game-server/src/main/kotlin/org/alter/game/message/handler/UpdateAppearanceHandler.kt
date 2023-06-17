package org.alter.game.message.handler

import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.UpdateAppearanceMessage
import org.alter.game.model.appearance.Appearance
import org.alter.game.model.appearance.Gender
import org.alter.game.model.World
import org.alter.game.model.entity.Client
import java.util.Arrays

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateAppearanceHandler : MessageHandler<UpdateAppearanceMessage> {

    override fun handle(client: Client, world: World, message: UpdateAppearanceMessage) {
        val gender = if (message.gender == 1) Gender.FEMALE else Gender.MALE
        val looks = message.appearance
        val colors = message.colors

        log(client, "Update appearance: gender=%s, appearance=%s, colors=%s", gender.toString(), Arrays.toString(looks), Arrays.toString(colors))
        client.queues.submitReturnValue(Appearance(looks, colors, gender))
    }
}