package gg.rsmod.util.concurrency

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.concurrent.Phaser

object PhasedTask {
    private val logger = KotlinLogging.logger {}

    fun run(
        phaser: Phaser,
        task: () -> Unit,
    ) {
        try {
            task()
        } catch (e: Exception) {
            logger.error("Error with phased task.", e)
        } finally {
            phaser.arriveAndDeregister()
        }
    }
}
