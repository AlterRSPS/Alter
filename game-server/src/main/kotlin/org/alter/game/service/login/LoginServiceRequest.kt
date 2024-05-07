package org.alter.game.service.login

import gg.rsmod.net.codec.login.LoginRequest
import net.rsprot.protocol.api.login.GameLoginResponseHandler
import net.rsprot.protocol.loginprot.incoming.util.AuthenticationType
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * Contains information required to process a [LoginRequest].
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class LoginServiceRequest(
    val world: World,
    val responseHandler: GameLoginResponseHandler<Client>,
    val block: LoginBlock<AuthenticationType<*>>
)