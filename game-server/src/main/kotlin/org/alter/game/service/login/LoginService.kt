package org.alter.game.service.login

import gg.rsmod.util.ServerProperties
import gg.rsmod.util.concurrency.ThreadFactoryBuilder
import io.github.oshai.kotlinlogging.KotlinLogging
import net.rsprot.protocol.api.login.GameLoginResponseHandler
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.entity.Client
import org.alter.game.service.Service
import org.alter.game.service.world.SimpleWorldVerificationService
import org.alter.game.service.world.WorldVerificationService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

/**
 * A [Service] that is responsible for handling incoming login requests.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class LoginService : Service {

    /**
     * The [LoginServiceRequest] requests that will be handled by our workers.
     */
    val requests = LinkedBlockingQueue<LoginServiceRequest>()

    private var threadCount = 1

    override fun init(
        server: Server,
        world: World,
        serviceProperties: ServerProperties,
    ) {
        threadCount = serviceProperties.getOrDefault("thread-count", 3)
    }

    override fun postLoad(
        server: Server,
        world: World,
    ) {

        val worldVerificationService =
            world.getService(
                WorldVerificationService::class.java,
                searchSubclasses = true,
            ) ?: SimpleWorldVerificationService()

        val executorService =
            Executors.newFixedThreadPool(
                threadCount,
                ThreadFactoryBuilder().setNameFormat(
                    "login-worker",
                ).setUncaughtExceptionHandler { t, e -> logger.error(e) { "Error with thread $t" } }.build(),
            )
        for (i in 0 until threadCount) {
            executorService.execute(LoginWorker(this, worldVerificationService))
        }
    }

    fun addLoginRequest(
        world: World,
        responseHandler: GameLoginResponseHandler<Client>,
        block: LoginBlock<*>,
    ) {
        val serviceRequest = LoginServiceRequest(world, responseHandler, block)
        requests.offer(serviceRequest)
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
