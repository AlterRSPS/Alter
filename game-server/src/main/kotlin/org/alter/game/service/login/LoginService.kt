package org.alter.game.service.login

import gg.rsmod.util.ServerProperties
import gg.rsmod.util.concurrency.ThreadFactoryBuilder
import io.github.oshai.kotlinlogging.KotlinLogging
import net.rsprot.protocol.api.login.GameLoginResponseHandler
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock
import org.alter.game.model.World
import org.alter.game.model.entity.Client
import org.alter.game.service.Service
import org.alter.game.service.serializer.PlayerSerializerService
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
     * The [PlayerSerializerService] implementation that will be used to decode
     * and encode the player data.
     */
    lateinit var serializer: PlayerSerializerService

    /**
     * The [LoginServiceRequest] requests that will be handled by our workers.
     */
    val requests = LinkedBlockingQueue<LoginServiceRequest>()

    private var threadCount = 1

    override fun init(server: org.alter.game.Server, world: World, serviceProperties: ServerProperties) {
        threadCount = serviceProperties.getOrDefault("thread-count", 3)
    }

    override fun postLoad(server: org.alter.game.Server, world: World) {
        serializer = world.getService(PlayerSerializerService::class.java, searchSubclasses = true)!!

        val worldVerificationService = world.getService(WorldVerificationService::class.java, searchSubclasses = true) ?: SimpleWorldVerificationService()

        val executorService = Executors.newFixedThreadPool(threadCount, ThreadFactoryBuilder().setNameFormat("login-worker").setUncaughtExceptionHandler { t, e -> logger.error("Error with thread $t", e) }.build())
        for (i in 0 until threadCount) {
            executorService.execute(LoginWorker(this, worldVerificationService))
        }
    }

    override fun bindNet(server: org.alter.game.Server, world: World) {
    }

    override fun terminate(server: org.alter.game.Server, world: World) {
    }

    fun addLoginRequest(world: World, responseHandler: GameLoginResponseHandler<Client>, block: LoginBlock<*>) {
        val serviceRequest = LoginServiceRequest(world, responseHandler, block)
        requests.offer(serviceRequest)
    }

    companion object {
        private val logger = KotlinLogging.logger{}
    }
}