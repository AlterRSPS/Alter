package org.alter.game.service.login

import net.rsprot.protocol.api.login.GameLoginResponseHandler
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock
import org.alter.game.model.World
import org.alter.game.model.entity.Client

/**
 * Contains information required to process a [LoginBlock].
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class LoginServiceRequest(
    val world: World,
    val responseHandler: GameLoginResponseHandler<Client>,
    val block: LoginBlock<*>,
)
