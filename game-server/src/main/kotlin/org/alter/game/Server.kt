package org.alter.game

import dev.openrune.cache.CacheManager
import gg.rsmod.util.ServerProperties
import gg.rsmod.util.Stopwatch
import io.github.oshai.kotlinlogging.KotlinLogging
import net.rsprot.protocol.common.client.OldSchoolClientType
import org.alter.game.fs.ObjectExamineHolder
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.entity.GroundItem
import org.alter.game.model.entity.Npc
import org.alter.game.model.skill.SkillSet
import org.alter.game.saving.PlayerDetails
import org.alter.game.saving.PlayerSaving
import org.alter.game.saving.formats.SaveFormatType
import org.alter.rscm.RSCM
import org.alter.game.rsprot.CacheJs5GroupProvider
import org.alter.game.rsprot.NetworkServiceFactory
import org.alter.game.service.xtea.XteaKeyService
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

/**
 * The [Server] is responsible for starting any and all games.
 *
 * @author Tom <rspsmods@gmail.com>
 */
@OptIn(ExperimentalStdlibApi::class)
class Server {
    /**
     * The properties specific to our API.
     */
    private val apiProperties = ServerProperties()

    /**
     * Prepares and handles any API related logic that must be handled
     * before the game can be launched properly.
     */
    fun startServer(apiProps: Path) {
        Thread.setDefaultUncaughtExceptionHandler { t, e -> e.printStackTrace() }
        val stopwatch = Stopwatch.createStarted()

        /*
         * Load the API property file.
         */
        apiProperties.loadYaml(apiProps.toFile())
        logger.info{"Preparing ${getApiName()}..."}

        /*
         * Inform the time it took to load the API related logic.
         */
        logger.info { "${getApiName()} loaded up in ${stopwatch.elapsed(TimeUnit.MILLISECONDS)}ms." }
    }

    /**
     * Prepares and handles any game related logic that was specified by the
     * user.
     *
     * Due to being decoupled from the API logic that will always be used, you
     * can start multiple servers with different game property files.
     */
    fun startGame(
        filestore: Path,
        gameProps: Path,
        devProps: Path?,
    ): World {
        val stopwatch = Stopwatch.createStarted()
        val individualStopwatch = Stopwatch.createUnstarted()

        /*
         * Load the game property file.
         */
        val initialLaunch = Files.deleteIfExists(Paths.get("../first-launch"))
        val gameProperties = ServerProperties()
        val devProperties = ServerProperties()
        gameProperties.loadYaml(gameProps.toFile())
        if (devProps != null && Files.exists(devProps)) {
            devProperties.loadYaml(devProps.toFile())
        }
        logger.info{"Loaded properties for ${gameProperties.get<String>("name")!!}."}

        /*
         * Create a game context for our configurations and services to run.
         */
        val gameContext =
            GameContext(
                initialLaunch = initialLaunch,
                name = gameProperties.get<String>("name")!!,
                revision = gameProperties.get<Int>("revision")!!,
                saveFormat = SaveFormatType.valueOf(gameProperties.get<String>("saveFormat")?: "JSON"),
                cycleTime = gameProperties.getOrDefault("cycle-time", 600),
                playerLimit = gameProperties.getOrDefault("max-players", 2048),
                /**
                 * @TODO Values do not output any context if any of them r null or throw exception on start
                 */
                home =
                    Tile(
                        gameProperties.get<Int>("home-x")!!,
                        gameProperties.get<Int>("home-z")!!,
                        gameProperties.getOrDefault("home-height", 0),
                    ),
                skillCount = gameProperties.getOrDefault("skill-count", SkillSet.DEFAULT_SKILL_COUNT),
                npcStatCount = gameProperties.getOrDefault("npc-stat-count", Npc.Stats.DEFAULT_NPC_STAT_COUNT),
                runEnergy = gameProperties.getOrDefault("run-energy", true),
                gItemPublicDelay =
                    gameProperties.getOrDefault(
                        "gitem-public-spawn-delay",
                        GroundItem.DEFAULT_PUBLIC_SPAWN_CYCLES,
                    ),
                gItemDespawnDelay = gameProperties.getOrDefault("gitem-despawn-delay", GroundItem.DEFAULT_DESPAWN_CYCLES),
                preloadMaps = gameProperties.getOrDefault("preload-maps", false),
            )

        val devContext =
            DevContext(
                debugExamines = devProperties.getOrDefault("debug-examines", false),
                debugObjects = devProperties.getOrDefault("debug-objects", false),
                debugButtons = devProperties.getOrDefault("debug-buttons", false),
                debugItemActions = devProperties.getOrDefault("debug-items", false),
                debugMagicSpells = devProperties.getOrDefault("debug-spells", false),
                debugPackets = devProperties.getOrDefault("debug-packets", false)
            )

        System.setProperty("net.rsprot.protocol.internal.networkLogging", devContext.debugPackets.toString())

        PlayerDetails.init(gameContext)
        PlayerSaving.init(gameContext)

        /*
         * Load the file store.
         */
        individualStopwatch.reset().start()
        CacheManager.init(filestore, gameContext.revision)
        logger.info{"Loaded filestore from path ${filestore} in ${individualStopwatch.elapsed(TimeUnit.MILLISECONDS)}ms."}

        val world = World(gameContext, devContext)

        /*
         * Load the definitions.
         */
        ObjectExamineHolder.load()

        /*
         * Load the services required to run the server.
         */
        world.loadServices(this, gameProperties)

        val groupProvider = CacheJs5GroupProvider()
        groupProvider.load()
        val port = gameProperties.getOrDefault("game-port", 43594)
        val network = NetworkServiceFactory(groupProvider, world, listOf(port), listOf(OldSchoolClientType.DESKTOP))
        world.network = network.build()

        world.init()

        if (gameContext.preloadMaps) {
            /*
             * Preload region definitions.
             */
            world.getService(XteaKeyService::class.java)?.let { service ->
                world.definitions.loadRegions(world, world.chunks, service.validRegions)
            }
        }

        /**
         * Initialize RSCM
         */
        RSCM.init()

        /*
         * Load the privileges for the game.
         */
        individualStopwatch.reset().start()
        world.privileges.load(gameProperties)
        logger.info { "Loaded ${world.privileges.size()} privilege levels in ${individualStopwatch.elapsed(TimeUnit.MILLISECONDS)}ms." }

        /*
         * Load the plugins for game content.
         */
        individualStopwatch.reset().start()
        world.plugins.init(
            server = this,
            world = world,
            jarPluginsDirectory = gameProperties.getOrDefault("plugin-packed-path", "../plugins"),
        )
        logger.info {
            "Loaded ${DecimalFormat().format(world.plugins.getPluginCount())} plugins in ${individualStopwatch.elapsed(TimeUnit.MILLISECONDS)}ms."
        }

        /*
         * Post load world.
         */
        world.postLoad()

        /*
         * Inform the time it took to load up all non-network logic.
         */
        logger.info { "${gameProperties.get<String>("name")!!} loaded up in ${stopwatch.elapsed(TimeUnit.MILLISECONDS)}ms." }

        /*
         * Bind all service networks, if applicable.
         */
        world.bindServices(this)

        /*
         * Bind the game port.
         */
        world.network.start()
        logger.info { "Now listening for incoming connections on port $port..." }
        return world
    }

    /**
     * Gets the API-specific org name.
     */
    fun getApiName(): String = apiProperties.getOrDefault("org", "Alter")

    companion object {
        val logger = KotlinLogging.logger {}
    }
}
