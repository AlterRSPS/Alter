package org.alter.game.service.login

import io.github.oshai.kotlinlogging.KotlinLogging
import net.rsprot.protocol.common.client.OldSchoolClientType
import net.rsprot.protocol.loginprot.outgoing.LoginResponse
import net.rsprot.protocol.loginprot.outgoing.util.AuthenticatorResponse
import org.alter.game.message.DisconnectionHook
import org.alter.game.model.entity.Client
import org.alter.game.service.GameService
import org.alter.game.saving.PlayerLoadResult
import org.alter.game.saving.PlayerSaving
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

                val loadResult: PlayerLoadResult = PlayerSaving.loadPlayer(client, request.block)

                if (loadResult == PlayerLoadResult.LOAD_ACCOUNT || loadResult == PlayerLoadResult.NEW_ACCOUNT) {
                    world.getService(GameService::class.java)?.submitGameThreadJob {
                        val interceptedLoginResult =
                            verificationService.interceptLoginResult(
                                world,
                                client.uid,
                                client.username,
                                client.loginUsername,
                            )

                        if (interceptedLoginResult != null) {
                            request.responseHandler.writeFailedResponse(interceptedLoginResult)
                            logger.info { "${"User '{}' login denied with code {}."} ${client.username} $interceptedLoginResult" }
                        } else if (client.register()) {
                            request.responseHandler.writeSuccessfulResponse(
                                LoginResponse.Ok(
                                    authenticatorResponse = AuthenticatorResponse.NoAuthenticator,
                                    staffModLevel = client.privilege.id,
                                    playerMod = true,
                                    index = client.index,
                                    member = true,
                                    accountHash = 0,
                                    userId = 0,
                                    userHash = 0,
                                ),
                                request.block,
                            ).apply {
                                if (this == null) {
                                    return@apply
                                }
                                client.session = this
                                client.playerInfo = client.world.network.playerInfoProtocol.alloc(client.index, OldSchoolClientType.DESKTOP)
                                client.npcInfo = client.world.network.npcInfoProtocol.alloc(client.index, OldSchoolClientType.DESKTOP)
                                client.worldEntityInfo =
                                    client.world.network.worldEntityInfoProtocol.alloc(
                                        client.index,
                                        OldSchoolClientType.DESKTOP,
                                    )
                                setDisconnectionHook(DisconnectionHook(client))
                                client.login()
                            }
                        } else {
                            request.responseHandler.writeFailedResponse(LoginResponse.InvalidSave)
                            logger.info { "${"User '{}' login denied with code {}."} ${client.username} ${LoginResponse.InvalidSave}" }
                        }
                    }
                } else {
                    val errorCode =
                        when (loadResult) {
                            PlayerLoadResult.INVALID_CREDENTIALS -> LoginResponse.InvalidUsernameOrPassword
                            PlayerLoadResult.INVALID_RECONNECTION -> LoginResponse.BadSessionId
                            PlayerLoadResult.MALFORMED -> LoginResponse.Locked
                            else -> LoginResponse.InvalidSave
                        }
                    request.responseHandler.writeFailedResponse(errorCode)
                    logger.info { "${"User '{}' login denied with code {}."} ${client.username} $loadResult" }
                }
            } catch (e: Exception) {
                logger.error(e) { "Error when handling request from ${request.block.username}." }
            }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
