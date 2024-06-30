package org.alter.game.service.world

import net.rsprot.protocol.loginprot.outgoing.LoginResponse
import org.alter.game.model.PlayerUID
import org.alter.game.model.World
import org.alter.game.service.Service

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface WorldVerificationService : Service {

    /**
     * Intercept the login result on a player log-in.
     *
     * @return null if the player can log in successfully without
     */
    fun interceptLoginResult(
        world: World,
        uid: PlayerUID,
        displayName: String,
        loginName: String,
    ): LoginResponse?
}
