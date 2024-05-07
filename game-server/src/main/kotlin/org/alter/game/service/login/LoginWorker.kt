package org.alter.game.service.login

import gg.rsmod.net.codec.login.LoginResultType
import io.github.oshai.kotlinlogging.KotlinLogging
import net.rsprot.protocol.common.client.OldSchoolClientType
import net.rsprot.protocol.loginprot.outgoing.LoginResponse
import net.rsprot.protocol.loginprot.outgoing.util.AuthenticatorResponse
import org.alter.game.model.entity.Client
import org.alter.game.service.GameService
import org.alter.game.service.serializer.PlayerLoadResult
import org.alter.game.service.world.WorldVerificationService

/**
 * A worker for the [LoginService] that is responsible for handling the most
 * recent, non-handled [LoginServiceRequest] from its [boss].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class LoginWorker(private val boss: LoginService, private val verificationService: WorldVerificationService) : Runnable {

    override fun run() {
        while (true) {
            val request = boss.requests.take()
            try {
                val world = request.world

                val client = Client.fromRequest(world, request.responseHandler, request.block)
//                if(request.block.authentication is AuthenticationType.PasswordAuthentication) {
//
//                }
//                if(request.block.authentication is AuthenticationType.TokenAuthentication) {
//
//                }
//                if(request.block.authentication is XteaKey) {
//
//                }
                val loadResult: PlayerLoadResult = boss.serializer.loadClientData(client, request.block)

                if (loadResult == PlayerLoadResult.LOAD_ACCOUNT || loadResult == PlayerLoadResult.NEW_ACCOUNT) {
                    world.getService(GameService::class.java)?.submitGameThreadJob {
                        val interceptedLoginResult = verificationService.interceptLoginResult(world, client.uid, client.username, client.loginUsername)
                        val loginResult: LoginResultType = interceptedLoginResult ?: if (client.register()) {
                            LoginResultType.ACCEPTABLE
                        } else {
                            LoginResultType.COULD_NOT_COMPLETE_LOGIN
                        }
                        if (loginResult == LoginResultType.ACCEPTABLE) {
                            request.responseHandler.writeSuccessfulResponse(LoginResponse.Ok(
                                authenticatorResponse = AuthenticatorResponse.NoAuthenticator,
                                staffModLevel = client.privilege.id,
                                playerMod = true,
                                index = client.index,
                                member = true,
                                accountHash = 0,
                                userId = 0,
                                userHash = 0,
                            ), request.block) {
                                it?.apply {
                                    client.session = this
                                    client.playerInfo = client.world.network.playerInfoProtocol.alloc(client.index, OldSchoolClientType.DESKTOP)
                                    client.npcInfo = client.world.network.npcInfoProtocol.alloc(client.index, OldSchoolClientType.DESKTOP)
                                    client.login()
                                }
                            }
                        } else {
                            request.responseHandler.writeFailedResponse(LoginResponse.InvalidSave)
                            logger.info("User '{}' login denied with code {}.", client.username, loginResult)
                        }
                    }
                } else {
                    val errorCode = when (loadResult) {
                        PlayerLoadResult.INVALID_CREDENTIALS -> LoginResponse.InvalidUsernameOrPassword
                        PlayerLoadResult.INVALID_RECONNECTION -> LoginResponse.BadSessionId
                        PlayerLoadResult.MALFORMED -> LoginResponse.Locked
                        else -> LoginResponse.InvalidSave
                    }
                    request.responseHandler.writeFailedResponse(errorCode)
                    logger.info("User '{}' login denied with code {}.", client.username, loadResult)
                }
            } catch (e: Exception) {
                logger.error("Error when handling request from ${request.block.username}.", e)
            }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger{}
    }
}