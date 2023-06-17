package org.alter.game.service.login

import org.alter.game.model.World
import gg.rsmod.net.codec.login.LoginRequest

/**
 * Contains information required to process a [LoginRequest].
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class LoginServiceRequest(val world: World, val login: LoginRequest)