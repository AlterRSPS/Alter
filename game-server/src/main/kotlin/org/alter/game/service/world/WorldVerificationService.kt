package org.alter.game.service.world

import gg.rsmod.util.ServerProperties
import net.rsprot.protocol.loginprot.outgoing.LoginResponse
import org.alter.game.model.PlayerUID
import org.alter.game.model.World
import org.alter.game.service.Service

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface WorldVerificationService : Service {

    override fun init(server: org.alter.game.Server, world: World, serviceProperties: ServerProperties) {
    }

    override fun postLoad(server: org.alter.game.Server, world: World) {
    }

    override fun bindNet(server: org.alter.game.Server, world: World) {
    }

    override fun terminate(server: org.alter.game.Server, world: World) {
    }

    /**
     * Intercept the login result on a player log-in.
     *
     * @return null if the player can log in successfully without
     */
    fun interceptLoginResult(world: World, uid: PlayerUID, displayName: String, loginName: String): LoginResponse?
}