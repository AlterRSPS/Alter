@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.alter.game.service

import gg.rsmod.util.ServerProperties
import gg.rsmod.util.concurrency.ThreadFactoryBuilder
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import org.alter.game.model.World
import org.alter.game.task.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * The service used to schedule and execute logic needed for the game to run properly.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class GameService : Service {
    /**
     * The associated world with our current game.
     */
    lateinit var world: World

    /**
     * The max amount of incoming [org.alter.game.message.Message]s that can be
     * handled per cycle.
     */
    var maxMessagesPerCycle = 0

    /**
     * The scheduler for our game cycle logic as well as coroutine dispatcher.
     */
    private val executor: ScheduledExecutorService =
        Executors.newSingleThreadScheduledExecutor(
            ThreadFactoryBuilder()
                .setNameFormat("game-context")
                .setUncaughtExceptionHandler { t, e -> logger.error(e) { "Error with thread $t" } }
                .build(),
        )

    /**
     * A list of jobs that will be executed on the next cycle after being
     * submitted.
     */
    private val gameThreadJobs = ConcurrentLinkedQueue<() -> Unit>()

    /**
     * The amount of ticks that have gone by since the last debug log.
     */
    private var debugTick = 0

    /**
     * The total time, in milliseconds, that the past [TICKS_PER_DEBUG_LOG]
     * cycles have taken to complete.
     */
    private var cycleTime = 0

    /**
     * The Kotlin Coroutine dispatcher to submit suspendable plugins.
     */
    val dispatcher: CoroutineDispatcher = executor.asCoroutineDispatcher()

    /**
     * The amount of time, in milliseconds, that each [GameTask] has taken away
     * from the game cycle.
     */
    private val taskTimes = Object2LongOpenHashMap<Class<GameTask>>()

    /**
     * The amount of time, in milliseconds, that [PlayerCycleTask]
     * has taken for each [org.alter.game.model.entity.Player].
     */
    internal val playerTimes = Object2LongOpenHashMap<String>()

    /**
     * The amount of active [org.alter.game.model.queue.QueueTask]s throughout
     * the [org.alter.game.model.entity.Player]s.
     */
    internal var totalPlayerQueues = 0

    /**
     * The amount of active [org.alter.game.model.queue.QueueTask]s throughout
     * the [org.alter.game.model.entity.Npc]s.
     */
    internal var totalNpcQueues = 0

    /**
     * The amount of active [org.alter.game.model.queue.QueueTask]s throughout
     * the [org.alter.game.model.World].
     */
    internal var totalWorldQueues = 0

    /**
     * A list of tasks that will be executed per game cycle.
     */
    private val tasks = mutableListOf<GameTask>()

    /**
     * This flag indicates that the game cycles should pause.
     *
     * Should not be used without proper knowledge of how it works!
     */
    internal var pause = false

    override fun init(
        server: org.alter.game.Server,
        world: World,
        serviceProperties: ServerProperties,
    ) {
        this.world = world
        populateTasks()
        maxMessagesPerCycle = serviceProperties.getOrDefault("messages-per-cycle", 30)
    }

    override fun bindNet(
        server: org.alter.game.Server,
        world: World,
    ) {
        executor.scheduleAtFixedRate(this::cycle, 0, world.gameContext.cycleTime.toLong(), TimeUnit.MILLISECONDS)
    }

    private fun populateTasks() {
        tasks.addAll(
            arrayOf(
                MessageHandlerTask(),
                QueueHandlerTask(),
                NpcCycleTask(),
                PlayerCycleTask(),
                ChunkCreationTask(),
                WorldRemoveTask(),
                SequentialSynchronizationTask(),
            ),
        )
    }

    /**
     * Submits a job that must be performed on the game-thread.
     */
    fun submitGameThreadJob(job: Function0<Unit>) {
        gameThreadJobs.offer(job)
    }

    private fun cycle() {
        if (pause) {
            return
        }
        val start = System.currentTimeMillis()

        /*
         * Clear the time it has taken to complete [GameTask]s from last cycle.
         */
        taskTimes.clear()
        playerTimes.clear()

        /*
         * Execute any logic jobs that were submitted.
         */
        gameThreadJobs.forEach { job ->
            try {
                job()
            } catch (e: Exception) {
                logger.error(e) { "Error executing game-thread job." }
            }
        }
        /*
         * Reset the logic jobs as they have been completed.
         */
        gameThreadJobs.clear()

        /*
         * Go over the [tasks] and execute their logic. Log the time it took
         * each [GameTask] to complete. Some of the tasks may also calculate
         * their time for each player so that we can have the amount of time,
         * in milliseconds, that each player took to perform certain tasks.
         */
        tasks.forEach { task ->
            val taskStart = System.currentTimeMillis()
            try {
                task.execute(world, this)
            } catch (e: Exception) {
                logger.error(e) { "Error with task ${task.javaClass.simpleName}." }
            }
            taskTimes[task.javaClass] = System.currentTimeMillis() - taskStart
        }
        world.cycle()

        /*
         * Calculate the time, in milliseconds, it took for this cycle to complete
         * and add it to [cycleTime].
         */
        cycleTime += (System.currentTimeMillis() - start).toInt()

        if (debugTick++ >= TICKS_PER_DEBUG_LOG) {
            val freeMemory = Runtime.getRuntime().freeMemory()
            val totalMemory = Runtime.getRuntime().totalMemory()
            val maxMemory = Runtime.getRuntime().maxMemory()

            /*
             * Description:
             *
             * Cycle time:
             * the average time it took for a game cycle to
             * complete the last [TICKS_PER_DEBUG_LOG] game cycles.
             *
             * Entities:
             * The amount of entities in the world.
             * p: players
             * n: npcs
             *
             * Map:
             * The amount of map entities that are currently active.
             * c: chunks [org.alter.game.model.region.Chunk]
             * r: regions
             * i: instanced maps [org.alter.game.model.instance.InstancedMap]
             *
             * Queues:
             * The amount of plugins that are being executed on this exact
             * game cycle.
             * p: players
             * n: npcs
             * w: world
             *
             * Mem Usage:
             * Memory usage statistics.
             * U: used memory, in megabytes
             * R: reserved memory, in megabytes
             * M: max memory available, in megabytes
             */
            logger.info("[Cycle time: {}ms] [Entities: {}p / {}n] [Map: {}c / {}r / {}i] [Queues: {}p / {}n / {}w] [Mem usage: U={}MB / R={}MB / M={}MB].",
                   cycleTime / TICKS_PER_DEBUG_LOG, world.players.count(), world.npcs.count(),
                   world.chunks.getActiveChunkCount(), world.chunks.getActiveRegionCount(), world.instanceAllocator.activeMapCount,
                   totalPlayerQueues, totalNpcQueues, totalWorldQueues,
                   (totalMemory - freeMemory) / (1024 * 1024), totalMemory / (1024 * 1024), maxMemory / (1024 * 1024))
            debugTick = 0
            cycleTime = 0
        }

        val freeTime = world.gameContext.cycleTime - (System.currentTimeMillis() - start)
        if (freeTime < 0) {
            /**
             * @TODO
             * If the cycle took more than [GameContext.cycleTime]ms, we log the
             * occurrence as well as the time each [GameTask] took to complete,
             * as well as how long each [org.alter.game.model.entity.Player] took
             * to process this cycle.
             */
            logger.error { "Cycle took longer than expected: ${(-freeTime) + world.gameContext.cycleTime}ms / ${world.gameContext.cycleTime}ms!" }
            logger.error { taskTimes.toList().sortedByDescending { (_, value) -> value }.toMap() }
            logger.error { playerTimes.toList().sortedByDescending { (_, value) -> value }.toMap() }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}

        /**
         * The amount of ticks that must go by for debug info to be logged.
         */
        private const val TICKS_PER_DEBUG_LOG = 10
    }
}
