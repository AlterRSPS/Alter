package org.alter.plugins.service.worldlist

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gg.rsmod.util.ServerProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.plugin.Plugin
import org.alter.game.service.Service
import org.alter.plugins.service.worldlist.io.WorldListChannelHandler
import org.alter.plugins.service.worldlist.io.WorldListChannelInitializer
import org.alter.plugins.service.worldlist.model.WorldEntry
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Triston Plummer ("Dread")
 *
 * @TODO Handles the initialisation of the network pipeline used for providing the client's world list. Perhaps in the future,
 * if we decide to actually handle multiple worlds, this logic should be moved to a login service or some intermediate
 * service that can retrieve data from multiple worlds.
 */
class WorldListService : Service {
    val bootstrap = ServerBootstrap()

    /**
     * The port for this [WorldListService] to listen on
     */
    private var port: Int = 80

    /**
     * The [Path] to the configuration file holding the [WorldEntry] data.
     */
    private lateinit var path: Path

    /**
     * The current app's [WorldEntry].
     */
    @Volatile private lateinit var worldEntry: WorldEntry

    private lateinit var worldEntries: MutableList<WorldEntry>

    /**
     * The [ChannelFuture] for the network service
     */
    private lateinit var channelFuture: ChannelFuture

    /**
     * Initialises the [WorldListService] by checking that the world list configuration file exists,
     * and attempts to parse it.
     *
     * @param server            The server instance
     * @param world             The game world instance
     * @param serviceProperties The properties for this server
     */
    override fun init(
        server: Server,
        world: World,
        serviceProperties: ServerProperties,
    ) {
        port = serviceProperties.getOrDefault("port", 80)
        path = Paths.get(serviceProperties.getOrDefault("config-path", "../data/cfg/world.json"))

        // If the world configuration file doesn't exist, spit out a warning and do nothing
        if (!Files.exists(path)) {
            logger.warn("World list configuration file does not exist: $path")
            return
        }

        parse()
    }

    /**
     * Parses the world list
     */
    private fun parse() {
        Files.newBufferedReader(path).use { reader ->
            val worldEntri: List<WorldEntry> = Gson().fromJson(reader, object : TypeToken<List<WorldEntry>>() {}.type)
            worldEntries = worldEntri.toMutableList()
        }
    }

    /**
     * Binds the network used for serving the world list
     *
     * @param server    The [Server] context
     * @param world     The [World] instance
     */
    override fun bindNet(
        server: Server,
        world: World,
    ) {
        // The inbound channel handler for the world list protocol
        val handler = WorldListChannelHandler(worldEntries)

        // Bind the world list network pipeline
        val bootstrap = bootstrap.clone()
        bootstrap.childHandler(WorldListChannelInitializer(handler))
        channelFuture = bootstrap.bind(port).syncUninterruptibly()
        logger.info("World list service listening on port $port, ${worldEntries.size} loaded.")
    }

    /**
     * Binds the plugin logic for incrementing and decrementing player count
     *
     * @param server    The [Server] instance
     * @param world     The [World] instance
     */
    override fun postLoad(
        server: Server,
        world: World,
    ) {
        val plugins = world.plugins
        plugins.bindLogin(incrementPlayerCount)
        plugins.bindLogout(decrementPlayerCount)
    }

    /**
     * Terminates this [WorldListService]
     *
     * @param server    The [Server] instance
     * @param world     The [World] instance
     */
    override fun terminate(
        server: Server,
        world: World,
    ) {
        channelFuture.channel().close().syncUninterruptibly()
    }

    /**
     * Increments the player count for every entry in the world list
     */
    private val incrementPlayerCount: Plugin.() -> Unit = {
        // @TODO Add support for multi worlds
        // worldEntry.players++
    }

    /**
     * Decrements the player count for every entry in the world list
     */
    private val decrementPlayerCount: Plugin.() -> Unit = {
        // @TODO Add support for multi worlds
        // worldEntry.players--
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
