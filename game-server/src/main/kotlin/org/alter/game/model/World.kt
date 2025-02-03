package org.alter.game.model

import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.CacheManager.getNpc
import gg.rsmod.util.ServerProperties
import gg.rsmod.util.Stopwatch
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import net.rsprot.protocol.api.NetworkService
import net.rsprot.protocol.game.outgoing.logout.Logout
import net.rsprot.protocol.game.outgoing.misc.client.UpdateRebootTimer
import org.alter.game.DevContext
import org.alter.game.GameContext
import org.alter.game.Server
import org.alter.game.fs.DefinitionSet
import org.alter.game.fs.ObjectExamineHolder
import org.alter.game.model.attr.AttributeMap
import org.alter.game.model.collision.isClipped
import org.alter.game.model.combat.NpcCombatDef
import org.alter.game.model.entity.*
import org.alter.game.model.instance.InstancedMapAllocator
import org.alter.game.model.priv.PrivilegeSet
import org.alter.game.model.queue.QueueTask
import org.alter.game.model.queue.QueueTaskSet
import org.alter.game.model.queue.TaskPriority
import org.alter.game.model.queue.impl.WorldQueueTaskSet
import org.alter.game.model.region.ChunkSet
import org.alter.game.model.shop.Shop
import org.alter.game.model.timer.TimerMap
import org.alter.game.plugin.Plugin
import org.alter.game.plugin.PluginRepository
import org.alter.game.service.GameService
import org.alter.game.service.Service
import org.alter.game.service.xtea.XteaKeyService
import org.rsmod.routefinder.LineValidator
import org.rsmod.routefinder.RouteFinding
import org.rsmod.routefinder.StepValidator
import org.rsmod.routefinder.collision.CollisionFlagMap
import org.rsmod.routefinder.flag.CollisionFlag
import org.rsmod.routefinder.reach.ReachStrategy
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * The game world, which stores all the entities and nodes that the world
 * needs to keep track of.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class World(val gameContext: GameContext, val devContext: DevContext) {
    lateinit var network: NetworkService<Client>

    /**
     * The [DefinitionSet] that holds general filestore data.
     */
    val definitions = DefinitionSet()

    lateinit var settings: Any

    /**
     * A collection of our [Service]s specified in our game [ServerProperties]
     * files.
     */
    internal val services = mutableListOf<Service>()

    lateinit var coroutineDispatcher: CoroutineDispatcher

    internal var queues: QueueTaskSet = WorldQueueTaskSet()

    val players = PawnList(arrayOfNulls<Player>(gameContext.playerLimit))

    val npcs = PawnList(arrayOfNulls<Npc>(Short.MAX_VALUE.toInt()))

    val chunks = ChunkSet(this)

    val collision: CollisionFlagMap = CollisionFlagMap()

    val lineValidator = LineValidator(collision)
    val stepValidator = StepValidator(collision)
    val smartRouteFinder = RouteFinding(collision)
    val dumbRouteFinder = RouteFinding
    val reachStrategy = ReachStrategy

    fun canTraverse(
        source: Tile,
        direction: Direction,
        pawn: Pawn,
        srcSize: Int = 1,
    ): Boolean {
        val nextTile = source.step(direction)
        val collisionFlags = collision[nextTile.x, nextTile.z, nextTile.height]

        return if (pawn is Npc && pawn.canSwim) {
            // Allow movement if the BLOCK_WALK flag is set, regardless of other flags
            (collisionFlags and CollisionFlag.BLOCK_WALK) != 0
        } else {
            stepValidator.canTravel(
                level = source.height,
                x = source.x,
                z = source.z,
                offsetX = direction.getDeltaX(),
                offsetZ = direction.getDeltaZ(),
                size = srcSize,
            )
        }
    }

    val instanceAllocator = InstancedMapAllocator()

    /**
     * The plugin repository that's responsible for storing all the plugins found.
     */
    var plugins = PluginRepository(this)

    /**
     * The [PrivilegeSet] that is attached to our game.
     */
    val privileges = PrivilegeSet()

    /**
     * A cached value for [org.alter.game.service.xtea.XteaKeyService] since it
     * is used frequently and in performance critical code. This value is set
     * when [XteaKeyService.init] is called.
     */
    var xteaKeyService: XteaKeyService? = null

    /**
     * A [Random] implementation used for pseudo-random purposes through-out
     * the game world.
     */
    val random: Random = SecureRandom()

    /**
     * The amount of game cycles that have gone by since the world was first
     * initialized. This can reset back to [0], if it's signalled to overflow
     * any time soon.
     */
    var currentCycle = 0

    /**
     * Multi-threaded path-finding should be reserved for when the average cycle
     * time is 1-2ms+. This is due to the nature of how the system and game cycles
     * work.
     *
     * Explanation:
     * The path-finder thread tries to calculate the path when the [Pawn.walkTo]
     * method is called, this happens on the following tasks:
     *
     * Plugin handler: a piece of content needs the player to walk somewhere
     * Message handler: the player's client is requesting to move
     *
     * The [org.alter.game.model.path.FutureRoute.completed] flag is checked on
     * the player pre-synchronization task, right before [MovementQueue.cycle]
     * is called. If the future route is complete, the path is added to the
     * player's movement queue and data is then sent to clients on the player
     * synchronization task.
     *
     * Due to this design, it is likely that the [org.alter.game.model.path.FutureRoute]
     * will not finish calculating the path if the time in between the [Pawn.walkTo]
     * being called and player pre-synchronization task being executed is fast enough
     *
     * From anecdotal experience, once the average cycle time reaches about 1-2ms+,
     * the multi-threaded path-finding becomes more responsive. However, if the
     * average cycle time is <= 0ms, the path-finder can take one cycle (usually)
     * to complete; this is not because the code is unoptimized - it is because
     * the future route has a window of "total cycle time taken" per cycle
     * to complete.
     *
     * Say a cycle took 250,000 nanoseconds to complete. This means the player
     * pre-synchronization task has already been executed within that time frame.
     * This being the case, the server already checked to see if the future route
     * has completed in those 250,000 nanoseconds. Though the path-finder isn't
     * slow, it's certainly not that fast. So now, the server has to wait until
     * next tick to check if the future route was successful (usually the case,
     * since a whole 600ms have now gone by).
     */
    internal var multiThreadPathFinding = false

    /**
     * World timers.
     *
     * @see TimerMap
     */
    val timers = TimerMap()

    /**
     * World attributes.
     *
     * @see AttributeMap
     */
    val attr = AttributeMap()

    /**
     * A local collection of [GroundItem]s that are currently spawned. We do
     * not use [ChunkSet]s to iterate through this as it takes quite a bit of
     * time to do so every cycle.
     */
    private val groundItems = ObjectArrayList<GroundItem>()

    /**
     * Any ground item that should be spawned in the future. For example, when
     * a 'permanent' ground item is despawned, it will be added here to be spawned
     * after a set amount of cycles.
     */
    private val groundItemQueue = ObjectArrayList<GroundItem>()

    /**
     * The amount of time before a server reboot takes place, in game cycles.
     */
    var rebootTimer = -1

    internal fun init() {
        getService(GameService::class.java)?.let { service ->
            coroutineDispatcher = service.dispatcher
        }
    }

    /**
     * Executed after the server has initialised everything, but is not yet bound
     * to a network port.
     */
    internal fun postLoad() {
        plugins.executeWorldInit(this)
    }



    /**
     * Executed every game cycle.
     */
    internal fun cycle() {
        if (currentCycle++ >= Int.MAX_VALUE - 1) {
            currentCycle = 0
            logger.info {"World cycle has been reset." }
        }

        /*
         * Copy the timers to a mutable map just in case a timer has to modify
         * the [timers] during its execution, which isn't uncommon.
         */
        val timersCopy = timers.getTimers().toMutableMap()
        timersCopy.forEach { key, time ->
            if (time <= 0) {
                plugins.executeWorldTimer(this, key)
                if (!timers.has(key)) {
                    timers.remove(key)
                }
            }
        }

        /*
         * Tick all timers down by one cycle.
         */
        timers.getTimers().entries.forEach { timer -> timer.setValue(timer.value - 1) }

        /*
         * Cycle through ground items to handle any despawn or respawn.
         */

        /*
         * Any ground item that should be removed this cycle will be added here.
         */
        val groundItemRemoval = ObjectOpenHashSet<GroundItem>(0)

        /*
         * Iterate through our registered [groundItems] and increment their current
         * cycle.
         */
        val groundItemIterator = groundItems.iterator()
        while (groundItemIterator.hasNext()) {
            val groundItem = groundItemIterator.next()

            groundItem.currentCycle++

            if (groundItem.isPublic() && groundItem.currentCycle >= gameContext.gItemDespawnDelay && groundItem.respawnCycles == -1) {
                /*
                 * If the ground item is public and its cycle count has reached the
                 * despawn delay set by our game, we add it to our removal queue.
                 */
                groundItemRemoval.add(groundItem)
            } else if (!groundItem.isPublic() && groundItem.currentCycle >= gameContext.gItemPublicDelay) {
                /*
                 * If the ground item is not public, but its cycle count has
                 * reached the public delay set by our game, we make it public.
                 */
                groundItem.removeOwner()
                groundItem.ownerShipType = 0
                /**
                 * @TODO Hmm weird cuz it just vanished and appeared.
                 * And second : When [gItemPublicDelay] matches currentCycle some different update happens need to do more research on it.
                 */
                groundItem.timeUntilPublic = 0
                groundItem.timeUntilDespawn -= gameContext.gItemPublicDelay
                chunks.get(groundItem.tile)?.let { chunk ->
                    chunk.removeEntity(this, groundItem, groundItem.tile)
                    chunk.addEntity(this, groundItem, groundItem.tile)
                }
            }
        }

        /*
         * We now remove any ground item that was queued for removal.
         * We also check to see if they should respawn after a set amount
         * of cycles; if so, we append it to our [groundItemQueue] to be
         * spawned at a later point in time.
         */
        groundItemRemoval.forEach { item ->
            remove(item)
            if (item.respawnCycles > 0) {
                item.currentCycle = 0
                groundItemQueue.add(item)
            }
        }

        /*
         * Go over our [groundItemQueue] and respawn any ground item that has
         * met the respawn criteria.
         */
        val groundItemQueueIterator = groundItemQueue.iterator()
        while (groundItemQueueIterator.hasNext()) {
            val item = groundItemQueueIterator.next()
            item.currentCycle++
            if (item.currentCycle >= item.respawnCycles) {
                item.currentCycle = 0
                spawn(item)
                groundItemQueueIterator.remove()
            }
        }

        /*
         * Cycle through shops for their resupply ticks.
         */
        plugins.shops.values.forEach { it.cycle(this) }

        /*
         * Cycle through instanced maps.
         */
        instanceAllocator.cycle(this)

        if (rebootTimer > 0) {
            rebootTimer--

            if (rebootTimer == 0) {
                for (i in 0 until players.capacity) {
                    players[i]?.let { player ->
                        player.handleLogout()
                        player.write(Logout)
                        player.channelFlush()
                    }
                }
            }
        }
    }

    /**
     * Sends the reboot timer to all registered players.
     */
    fun sendRebootTimer(cycles: Int = rebootTimer) {
        players.forEach { p ->
            p.write(UpdateRebootTimer(cycles))
        }
    }

    fun register(p: Player): Boolean {
        val registered = players.add(p)
        if (registered) {
            p.lastIndex = p.index
            return true
        }
        return false
    }

    fun unregister(p: Player) {
        network.playerInfoProtocol.dealloc(p.playerInfo)
        network.npcInfoProtocol.dealloc(p.npcInfo)
        network.worldEntityInfoProtocol.dealloc(p.worldEntityInfo)

        players.remove(p)
        chunks.get(p.tile)?.removeEntity(this, p, p.tile)
    }

    fun spawn(npc: Npc): Boolean {
        val added = npcs.add(npc)
        if (added) {
            npc.initAvatar(
                network.npcAvatarFactory.alloc(
                    npc.index,
                    npc.id,
                    npc.tile.height,
                    npc.tile.x,
                    npc.tile.z,
                    0,
                    npc.faceDirection.orientationValue,
                )
            )
            setNpcDefaults(npc)
            plugins.executeNpcSpawn(npc)
        }
        return added
    }

    fun remove(npc: Npc) {
        npcs.remove(npc)
        chunks.get(npc.tile)?.removeEntity(this, npc, npc.tile)
    }

    fun spawn(obj: GameObject) {
        val tile = obj.tile
        val chunk = chunks.getOrCreate(tile)

        val oldObj =
            chunk.getEntities<GameObject>(
                tile,
                EntityType.STATIC_OBJECT,
                EntityType.DYNAMIC_OBJECT,
            ).firstOrNull { it.type == obj.type }
        if (oldObj != null) {
            chunk.removeEntity(this, oldObj, tile)
        }
        chunk.addEntity(this, obj, tile)
    }

    fun remove(obj: GameObject) {
        val tile = obj.tile
        val chunk = chunks.getOrCreate(tile)

        chunk.removeEntity(this, obj, tile)
    }

    fun spawn(item: GroundItem) {
        val tile = item.tile
        val chunk = chunks.getOrCreate(tile)
        val def = getItem(item.item)
        if (def.stackable) {
            val oldItem =
                chunk.getEntities<GroundItem>(tile, EntityType.GROUND_ITEM).firstOrNull {
                    it.item == item.item && it.ownerUID == item.ownerUID
                }
            if (oldItem != null) {
                val oldAmount = oldItem.amount
                val newAmount = Math.min(Int.MAX_VALUE.toLong(), item.amount.toLong() + oldItem.amount.toLong()).toInt()
                oldItem.amount = newAmount
                chunk.updateGroundItem(this, item, oldAmount, newAmount)
                return
            }
        }
        groundItems.add(item)
        chunk.addEntity(this, item, tile)
    }

    fun remove(item: GroundItem) {
        val tile = item.tile
        val chunk = chunks.getOrCreate(tile)

        groundItems.remove(item)
        chunk.removeEntity(this, item, tile)

        if (item.respawnCycles > 0) {
            item.currentCycle = 0
            groundItemQueue.add(item)
        }
    }

    fun spawn(projectile: Projectile) {
        val tile = projectile.tile
        val chunk = chunks.getOrCreate(tile)

        chunk.addEntity(this, projectile, tile)
    }

    /**
     * @TODO fix AreaSound baget
     */
    fun spawn(sound: AreaSound) {
        val tile = sound.tile
        val chunk = chunks.getOrCreate(tile)
        chunk.addEntity(this, sound, tile)
    }

    fun spawn(graphic: TileGraphic) {
        val tile = graphic.tile
        val chunk = chunks.getOrCreate(tile)

        chunk.addEntity(this, graphic, tile)
    }

    /**
     * Despawn entities in an area.
     */
    fun removeAll(area: Area) {
        for (i in 0 until npcs.capacity) {
            val npc = npcs[i] ?: continue
            if (area.contains(npc.tile)) {
                remove(npc)
            }
        }

        for (i in 0 until groundItems.size) {
            val item = groundItems[i] ?: continue
            if (area.contains(item.tile)) {
                remove(item)
            }
        }
    }

    fun isSpawned(obj: GameObject): Boolean =
        chunks.getOrCreate(obj.tile)
            .getEntities<GameObject>(obj.tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).contains(obj)

    fun isSpawned(item: GroundItem): Boolean =
        chunks.getOrCreate(item.tile).getEntities<GroundItem>(item.tile, EntityType.GROUND_ITEM).contains(item)

    /**
     * Get any [GroundItem] that matches the [predicate].
     *
     * @return null if no ground item meets the conditions of [predicate].
     */
    fun getGroundItem(predicate: (GroundItem) -> Boolean): GroundItem? = groundItems.firstOrNull { predicate(it) }

    /**
     * Gets the [GameObject] that is located on [tile] and has a
     * [GameObject.type] equal to [type].
     *
     * @return
     * null if no [GameObject] with [type] was found in [tile].
     */
    fun getObject(
        tile: Tile,
        type: Int,
    ): GameObject? =
        chunks.get(
            tile,
            createIfNeeded = true,
        )!!.getEntities<GameObject>(tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).firstOrNull {
            it.type == type
        }

    fun getPlayerForName(username: String): Player? {
        for (i in 0 until players.capacity) {
            val player = players[i] ?: continue
            if (player.username.equals(username, ignoreCase = true)) {
                return player
            }
        }
        return null
    }

    fun getPlayerForUid(uid: PlayerUID): Player? = players.firstOrNull { it.uid.value == uid.value }

    fun getShop(name: String): Shop? = plugins.shops.getOrDefault(name, null)

    fun getShop(shopId: Int): Shop? = plugins.shops.values.elementAt(shopId)

    fun getMultiCombatChunks(): Set<Int> = plugins.multiCombatChunks

    fun getMultiCombatRegions(): Set<Int> = plugins.multiCombatRegions

    fun random(boundInclusive: Int) = random.nextInt(boundInclusive + 1)

    fun random(range: IntRange): Int = random.nextInt(range.endInclusive - range.start + 1) + range.start

    fun randomDouble(): Double = random.nextDouble()

    fun chance(
        chance: Int,
        probability: Int,
    ): Boolean {
        check(chance in 1..probability) { "Chance must be within range of (0 - probability]" }
        return random.nextInt(probability) <= chance - 1
    }

    fun percentChance(chance: Double): Boolean {
        check(chance in 0.0..100.0) { "Chance must be within range of [0.0 - 100.0]" }
        return random.nextDouble() <= (chance / 100.0)
    }

    fun findRandomTileAround(
        centre: Tile,
        radius: Int,
        centreWidth: Int = 0,
        centreLength: Int = 0,
    ): Tile? {
        val tiles = mutableListOf<Tile>()
        for (x in -radius..radius) {
            for (z in -radius..radius) {
                if (x in 0 until centreWidth && z in 0 until centreLength) {
                    continue
                }
                tiles.add(centre.transform(x, z))
            }
        }
        val filtered = tiles.filter { tile -> !collision.isClipped(tile) }
        if (filtered.isNotEmpty()) {
            return filtered.random()
        }
        return null
    }

    fun queue(logic: suspend QueueTask.(CoroutineScope) -> Unit) {
        queues.queue(this, coroutineDispatcher, TaskPriority.STANDARD, logic)
    }

    fun executePlugin(
        ctx: Any,
        logic: (Plugin).() -> Unit,
    ) {
        val plugin = Plugin(ctx)
        logic(plugin)
    }

    fun sendExamine(
        p: Player,
        id: Int,
        type: ExamineEntityType,
    ) {
        val examine =
            when (type) {
                ExamineEntityType.ITEM -> getItem(id).examine
                ExamineEntityType.NPC -> getNpc(id).examine
                ExamineEntityType.OBJECT -> ObjectExamineHolder.EXAMINES.get(id)
            }

        if (examine != null) {
            val extension = if (devContext.debugExamines) " ($id)" else ""
            p.writeMessage(examine + extension)
        } else {
            logger.warn { "No examine info found for entity [$type, $id]" }
        }
    }

    fun setNpcDefaults(npc: Npc) {
        val combatDef = plugins.npcCombatDefs.getOrDefault(npc.id, null) ?: NpcCombatDef.DEFAULT
        npc.combatDef = combatDef
        npc.combatDef.bonuses.forEachIndexed { index, bonus -> npc.equipmentBonuses[index] = bonus }
        npc.respawns = combatDef.respawnDelay > 0
        npc.setCurrentHp(npc.combatDef.hitpoints)
    }

    /**
     * Gets the first service that can be found which meets the criteria of:
     *
     * When [searchSubclasses] is true: the service class must be assignable to the [type].
     * When [searchSubclasses] is false: the service class must be equal to the [type].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Service> getService(
        type: Class<out T>,
        searchSubclasses: Boolean = false,
    ): T? {
        if (searchSubclasses) {
            return services.firstOrNull { type.isAssignableFrom(it::class.java) } as T?
        }
        return services.firstOrNull { it::class.java == type } as T?
    }

    /**
     * Loads all the services listed on our game properties file.
     */
    internal fun loadServices(
        server: Server,
        gameProperties: ServerProperties,
    ) {
        val stopwatch = Stopwatch.createUnstarted()
        val foundServices = gameProperties.get<ArrayList<Any>>("services")!!
        foundServices.forEach { s ->
            val values = s as LinkedHashMap<*, *>
            val className = values["class"] as String
            val clazz = Class.forName(className).asSubclass(Service::class.java)!!
            val service = clazz.getDeclaredConstructor().newInstance()

            val properties = hashMapOf<String, Any>()
            values.filterKeys { it != "class" }.forEach { key, value ->
                properties[key as String] = value
            }

            stopwatch.reset().start()
            service.init(server, this, ServerProperties().loadMap(properties))
            stopwatch.stop()

            services.add(service)
            logger.info { "Initiated service '${service.javaClass.simpleName}' in ${stopwatch.elapsed(TimeUnit.MILLISECONDS)}ms." }
        }
        services.forEach { s -> s.postLoad(server, this) }
        logger.info { "Loaded ${services.size} game services." }
    }

    /**
     * Invoke network related logic for all services.
     */
    internal fun bindServices(server: Server) {
        services.forEach { it.bindNet(server, this) }
    }

    companion object {
        val logger = KotlinLogging.logger {}

        /**
         * If the [rebootTimer] is active and is less than this value, we will
         * begin to reject any log-in.
         */
        const val REJECT_LOGIN_REBOOT_THRESHOLD = 50
    }
}
