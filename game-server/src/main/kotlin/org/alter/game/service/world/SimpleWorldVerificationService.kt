package org.alter.game.service.world

import net.rsprot.protocol.loginprot.outgoing.LoginResponse
import org.alter.game.model.PlayerUID
import org.alter.game.model.World

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SimpleWorldVerificationService : WorldVerificationService {
    override fun interceptLoginResult(
        world: World,
        uid: PlayerUID,
        displayName: String,
        loginName: String,
    ): LoginResponse? =
        when {
            world.rebootTimer != -1 && world.rebootTimer < World.REJECT_LOGIN_REBOOT_THRESHOLD -> LoginResponse.UpdateInProgress
            world.getPlayerForName(displayName) != null -> LoginResponse.Duplicate
            world.players.count() >= world.players.capacity -> LoginResponse.IPLimit
            else -> null
        }
}
