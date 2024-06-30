package org.alter.game.rsprot

import net.rsprot.crypto.xtea.XteaKey
import net.rsprot.protocol.api.GameConnectionHandler
import net.rsprot.protocol.api.login.GameLoginResponseHandler
import net.rsprot.protocol.loginprot.incoming.util.AuthenticationType
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock
import org.alter.game.model.World
import org.alter.game.model.entity.Client
import org.alter.game.service.login.LoginService

class RsModConnectionHandler(private val world: World) : GameConnectionHandler<Client> {
    override fun onLogin(
        responseHandler: GameLoginResponseHandler<Client>,
        block: LoginBlock<AuthenticationType<*>>,
    ) {
        if (loginService == null) {
            loginService = world.getService(LoginService::class.java)
        }
        loginService!!.addLoginRequest(world, responseHandler, block)
    }

    override fun onReconnect(
        responseHandler: GameLoginResponseHandler<Client>,
        block: LoginBlock<XteaKey>,
    ) {
        if (loginService == null) {
            loginService = world.getService(LoginService::class.java)
        }
        loginService!!.addLoginRequest(world, responseHandler, block)
    }

    companion object {
        private var loginService: LoginService? = null
    }
}
