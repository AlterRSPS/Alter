package org.alter.game.model.entity

import kotlinx.coroutines.CoroutineScope
import net.rsprot.protocol.game.outgoing.misc.player.SetMapFlag
import org.alter.game.action.NpcDeathAction
import org.alter.game.action.PlayerDeathAction
import org.alter.game.event.Event
import org.alter.game.info.PlayerInfo
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.bits.INFINITE_VARS_STORAGE
import org.alter.game.model.bits.InfiniteVarsType
import org.alter.game.model.collision.CollisionManager
import org.alter.game.model.combat.DamageMap
import org.alter.game.model.path.FutureRoute
import org.alter.game.model.path.PathFindingStrategy
import org.alter.game.model.path.PathRequest
import org.alter.game.model.path.Route
import org.alter.game.model.path.strategy.BFSPathFindingStrategy
import org.alter.game.model.path.strategy.SimplePathFindingStrategy
import org.alter.game.model.queue.QueueTask
import org.alter.game.model.queue.QueueTaskSet
import org.alter.game.model.queue.TaskPriority
import org.alter.game.model.queue.impl.PawnQueueTaskSet
import org.alter.game.model.region.Chunk
import org.alter.game.model.timer.*
import org.alter.game.plugin.Plugin
import org.alter.game.service.log.LoggerService
import java.lang.ref.WeakReference
import java.util.*

/**
 * A controllable character in the world that is used by something, or someone,
 * for their own purpose.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class Pawn(val world: World) : Entity() {
    /**
     * The index assigned when this [Pawn] is successfully added to a [PawnList].
     */
    var index = -1

    /**
     * The 3D [Tile] that this pawn was standing on, in the last game cycle.
     */
    internal var lastTile: Tile? = null

    /**
     * The last tile that was set for the pawn's [org.alter.game.model.region.Chunk].
     */
    internal var lastChunkTile: Tile? = null

    /**
     * Whether or not this pawn can teleported this game cycle.
     */
    internal var moved = false

    /**
     * @see [MovementQueue]
     */
    internal val movementQueue by lazy { MovementQueue(this) }

    /**
     * The current directions that this pawn is moving.
     */
    internal var steps: MovementQueue.StepDirection? = null

    /**
     * The last [Direction] this pawn was facing.
     */
    var lastFacingDirection: Direction = Direction.SOUTH

    /**
     * A public getter property for [lastFacingDirection].
     */
    val faceDirection: Direction
        get() = lastFacingDirection

    /**
     * The current [LockState] which filters what actions this pawn can perform.
     */
    var lock = LockState.NONE

    /**
     * The attributes attached to the pawn.
     *
     * @see AttributeMap
     */
    val attr = AttributeMap()

    /**
     * The timers attached to the pawn.
     *
     * @see TimerMap
     */
    val timers = TimerMap()

    internal val queues: QueueTaskSet = PawnQueueTaskSet()

    /**
     * The equipment bonus for the pawn.
     */
    val equipmentBonuses = IntArray(14)

    /**
     * The current prayer icon that the pawn has active.
     */
    var prayerIcon = -1

    /**
     * Transmog is the action of turning into an npc. This value is equal to the
     * npc id of the npc you want to turn into, visually.
     */
    private var transmogId = -1

    /**
     * A list of pending [Hit]s.
     */
    private val pendingHits = mutableListOf<Hit>()

    /**
     * A [DamageMap] to keep track of who has dealt damage to this pawn.
     */
    val damageMap = DamageMap()

    /**
     * A flag which indicates if this pawn is visible to players in the world.
     */
    var invisible = false

    /**
     * The [FutureRoute] for the pawn, if any.
     * @see createPathFindingStrategy
     */
    private var futureRoute: FutureRoute? = null

    /**
     * Handles logic before any synchronization tasks are executed.
     */
    abstract fun cycle()

    fun isDead(): Boolean = getCurrentHp() == 0

    fun isAlive(): Boolean = !isDead()

    abstract fun isRunning(): Boolean

    abstract fun getSize(): Int

    abstract fun getCurrentHp(): Int

    abstract fun getMaxHp(): Int

    abstract fun setCurrentHp(level: Int)

    /**
     * Lock the pawn to the default [LockState.FULL] state.
     */
    fun lock() {
        lock = LockState.FULL
    }

    /**
     * Unlock the pawn and set it to [LockState.NONE] state.
     */
    fun unlock() {
        lock = LockState.NONE
    }

    /**
     * Checks if the pawn has any lock state set.
     */
    fun isLocked(): Boolean = lock != LockState.NONE
    fun getTransmogId(): Int = transmogId

    fun setTransmogId(transmogId: Int) {
        this.transmogId = transmogId
        if (entityType.isPlayer) {
            PlayerInfo(this as Player).syncAppearance()
        }
    }

    fun hasMoveDestination(): Boolean = futureRoute != null || movementQueue.hasDestination()

    fun stopMovement() {
        movementQueue.clear()
    }

    fun getCentreTile(): Tile = tile.transform(getSize() shr 1, getSize() shr 1)

    /**
     * Gets the tile the pawn is currently facing towards.
     */
    // Credits: Kris#1337
    fun getFrontFacingTile(
        target: Tile,
        offset: Int = 0,
    ): Tile {
        val size = (getSize() shr 1)
        val centre = getCentreTile()

        val granularity = 2048
        val lutFactor = (granularity / (Math.PI * 2)) // Lookup table factor

        val theta = Math.atan2((target.z - centre.z).toDouble(), (target.x - centre.x).toDouble())
        var angle = Math.toDegrees((((theta * lutFactor).toInt() + offset) and (granularity - 1)) / lutFactor)
        if (angle < 0) {
            angle += 360
        }
        angle = Math.toRadians(angle)

        val tx = Math.round(centre.x + (size * Math.cos(angle))).toInt()
        val tz = Math.round(centre.z + (size * Math.sin(angle))).toInt()
        return Tile(tx, tz, tile.height)
    }

    /**
     * Alias for [getFrontFacingTile] using a [Pawn] as the target tile.
     */
    fun getFrontFacingTile(
        target: Pawn,
        offset: Int = 0,
    ): Tile = getFrontFacingTile(target.getCentreTile(), offset)

    /**
     * Initiate combat with [target].
     */
    fun attack(target: Pawn) {
        resetInteractions()
        interruptQueues()

        attr[COMBAT_TARGET_FOCUS_ATTR] = WeakReference(target)

        /*
         * Players always have the default combat, and npcs will use default
         * combat <strong>unless</strong> they have a custom npc combat plugin
         * bound to their npc id.
         */
        if (entityType.isPlayer || this is Npc && !world.plugins.executeNpcCombat(this)) {
            world.plugins.executeCombat(this)
        }
    }

    fun addHit(hit: Hit) {
        pendingHits.add(hit)
    }

    fun clearHits() {
        pendingHits.clear()
    }

    /**
     * Handle a single cycle for [timers].
     */
    fun timerCycle() {
        val iterator = timers.getTimers().iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val key = entry.key
            val time = entry.value
            if (time <= 0) {
                if (key == RESET_PAWN_FACING_TIMER) {
                    resetFacePawn()
                } else {
                    try {
                        world.plugins.executeTimer(this, key)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                if (!timers.has(key)) {
                    iterator.remove()
                }
            }
        }

        timers.getTimers().entries.forEach { timer ->
            timer.setValue(timer.value - 1)
        }
    }

    /**
     * Handle a single cycle for [pendingHits].
     */
    fun hitsCycle() {
        val hitIterator = pendingHits.iterator()
        iterator@ while (hitIterator.hasNext()) {
            if (isDead()) {
                break
            }
            val hit = hitIterator.next()

            if (lock.delaysDamage()) {
                hit.damageDelay = Math.max(0, hit.damageDelay - 1)
                continue
            }

            if (hit.damageDelay-- == 0) {
                if (!hit.cancelCondition()) {
                    for (hitmark in hit.hitmarks) {
                        val hp = getCurrentHp()
                        if (hitmark.damage > hp) {
                            hitmark.damage = hp
                        }
                        /**
                         * @TODO
                         * Come up w some solution for attackerIndex, If im not mistaking at God wars / Nex you suppose to see other's HitSplats need to research this shit.
                         * As for hitbar types we add them later onto Npc Class, assign em from (set_npc_combat_def)
                         */
                        if (entityType.isNpc) {
                            val npc = this as Npc
                            npc.avatar.extendedInfo.addHitMark(
                                sourceIndex = hitmark.attackerIndex,
                                selfType = hitmark.type,
                                value = hitmark.damage,
                                delay = hit.clientDelay,
                            )
                            npc.avatar.extendedInfo.addHeadBar(
                                sourceIndex = hitmark.attackerIndex,
                                selfType = 0,
                                //endFill = 30,
                                startFill = calculateFill((((this.getCurrentHp().toDouble() - hitmark.damage) / this.getMaxHp().toDouble()) * 100), 30)
                            )
                        } else if (entityType.isPlayer) {
                            val player = this as Player
                            player.avatar.extendedInfo.addHitMark(
                                sourceIndex = hitmark.attackerIndex,
                                selfType = hitmark.type,
                                value = hitmark.damage,
                                delay = hit.clientDelay,
                            )
                            player.avatar.extendedInfo.addHeadBar(
                                sourceIndex = hitmark.attackerIndex,
                                selfType = 0,
                                //endFill = 30,
                                startFill = calculateFill((((this.getCurrentHp().toDouble() - hitmark.damage) / this.getMaxHp().toDouble()) * 100), 30)
                            )
                        }

                        /*
                         * Only lower the pawn's hp if they do not have infinite
                         * health enabled.
                         */
                        if (INFINITE_VARS_STORAGE.get(this, InfiniteVarsType.HP) == 0) {
                            setCurrentHp(hp - hitmark.damage)
                        }
                        /*
                         * If the pawn has less than or equal to 0 health,
                         * terminate all queues and begin the death logic.
                         */
                        if (getCurrentHp() <= 0) {
                            hit.actions.forEach { action -> action(hit) }
                            if (entityType.isPlayer) {
                                executePlugin(PlayerDeathAction.deathPlugin)
                            } else {
                                executePlugin(NpcDeathAction.deathPlugin)
                            }
                            hitIterator.remove()
                            break@iterator
                        }
                    }
                    hit.actions.forEach { action -> action(hit) }

                }
                hitIterator.remove()
            }
        }
        if (isDead() && pendingHits.isNotEmpty()) {
            pendingHits.clear()
        }
    }
    fun calculateFill(percentage: Double, width: Int): Int {
        val fill = (width * percentage / 100.0).toInt()
        return if (fill == 0 && percentage != 0.0) return 0 else fill
    }
    /**
     * Handle the [futureRoute] if necessary.
     */
    fun handleFutureRoute() {
        if (futureRoute?.completed == true && futureRoute?.strategy?.cancel == false) {
            val futureRoute = futureRoute!!
            walkPath(futureRoute.route.path, futureRoute.stepType, futureRoute.detectCollision)
            this.futureRoute = null
        }
    }

    /**
     * Walk to all the tiles specified in our [path] queue, using [stepType] as
     * the [MovementQueue.StepType].
     */
    fun walkPath(
        path: Queue<Tile>,
        stepType: MovementQueue.StepType,
        detectCollision: Boolean,
    ) {
        if (path.isEmpty()) {
            if (this is Player) {
                write(SetMapFlag(255, 255))
            }
            return
        }

        if (timers.has(FROZEN_TIMER)) {
            if (this is Player) {
                writeMessage(MAGIC_STOPS_YOU_FROM_MOVING)
            }
            return
        }

        if (timers.has(STUN_TIMER)) {
            return
        }

        movementQueue.clear()

        var tail: Tile? = null
        var next = path.poll()
        while (next != null) {
            movementQueue.addStep(next, stepType, detectCollision)
            val poll = path.poll()
            if (poll == null) {
                tail = next
            }
            next = poll
        }

        /*
         * If the tail is null (should never be unless we mess with code above), or
         * if the tail is the tile we're standing on, then we don't have to move at all!
         */
        if (tail == null || tail.sameAs(tile)) {
            if (this is Player) {
                write(SetMapFlag(255, 255))
            }
            movementQueue.clear()
            return
        }

        if (this is Player && lastKnownRegionBase != null) {
            write(SetMapFlag(tail.x - lastKnownRegionBase!!.x, tail.z - lastKnownRegionBase!!.z))
        }
    }

    fun walkTo(
        tile: Tile,
        stepType: MovementQueue.StepType = MovementQueue.StepType.NORMAL,
        detectCollision: Boolean = true,
    ) = walkTo(tile.x, tile.z, stepType, detectCollision)

    fun walkTo(
        x: Int,
        z: Int,
        stepType: MovementQueue.StepType = MovementQueue.StepType.NORMAL,
        detectCollision: Boolean = true,
    ) {
        /*
         * Already standing on requested destination.
         */
        if (tile.x == x && tile.z == z) {
            return
        }

        if (timers.has(FROZEN_TIMER)) {
            if (this is Player) {
                writeMessage(MAGIC_STOPS_YOU_FROM_MOVING)
            }
            return
        }

        if (timers.has(STUN_TIMER)) {
            return
        }

        val multiThread = world.multiThreadPathFinding
        val request = PathRequest.createWalkRequest(this, x, z, projectile = false, detectCollision = detectCollision)
        val strategy = createPathFindingStrategy(copyChunks = multiThread)

        /*
         * When using multi-thread path-finding, the [PathRequest.createWalkRequest]
         * must have the [tile] in sync with the game-thread, so we need to make sure
         * that in this cycle, the pawn's [tile] does not change. The easiest way to
         * do this is by clearing their movement queue. Though it can cause weird
         */
        if (multiThread) {
            movementQueue.clear()
        }
        futureRoute?.strategy?.cancel = true

        if (multiThread) {
            futureRoute = FutureRoute.of(strategy, request, stepType, detectCollision)
        } else {
            val route = strategy.calculateRoute(request)
            walkPath(route.path, stepType, detectCollision)
        }
    }

    suspend fun walkTo(
        it: QueueTask,
        tile: Tile,
        stepType: MovementQueue.StepType = MovementQueue.StepType.NORMAL,
        detectCollision: Boolean = true,
    ) = walkTo(it, tile.x, tile.z, stepType, detectCollision)

    suspend fun walkTo(
        it: QueueTask,
        x: Int,
        z: Int,
        stepType: MovementQueue.StepType = MovementQueue.StepType.NORMAL,
        detectCollision: Boolean = true,
    ): Route {
        /*
         * Already standing on requested destination.
         */
        if (tile.x == x && tile.z == z) {
            return Route(EMPTY_TILE_DEQUE, success = true, tail = Tile(tile))
        }
        val multiThread = world.multiThreadPathFinding
        val request = PathRequest.createWalkRequest(this, x, z, projectile = false, detectCollision = detectCollision)
        val strategy = createPathFindingStrategy(copyChunks = multiThread)

        movementQueue.clear()
        futureRoute?.strategy?.cancel = true

        if (multiThread) {
            futureRoute = FutureRoute.of(strategy, request, stepType, detectCollision)
            while (!futureRoute!!.completed) {
                it.wait(1)
            }
            return futureRoute!!.route
        }

        val route = strategy.calculateRoute(request)
        walkPath(route.path, stepType, detectCollision)
        return route
    }

    fun moveTo(
        x: Int,
        z: Int,
        height: Int = 0,
    ) {
        tile = Tile(x, z, height)
        movementQueue.clear()

        if (entityType.isNpc) {
            (this as Npc).avatar.teleport(height, x, z, true)
        } else if (entityType.isPlayer) {
            (this as Player).avatar.extendedInfo.setTempMoveSpeed(127)
        }
    }

    fun moveTo(tile: Tile) {
        moveTo(tile.x, tile.z, tile.height)
    }

    fun animate(
        id: Int,
        delay: Int = 0,
        interruptable: Boolean = false,
    ) {
        if (previouslySetAnim == -1 || interruptable) {
            if (entityType.isPlayer) {
                previouslySetAnim = id
            }
            if (this is Player) {
                world.plugins.executeOnAnimation(this, id)
            }
        }
        animateSend(-1, 0)
        animateSend(id, delay)
    }
    /**
     * @param id = Animation id
     * @param startDelay = when to start anim
     * @param interruptable = if Anim can be interrupted by other anim masks
     */
    fun animateSend(
        id: Int,
        startDelay: Int = 0,
    ) {
        if (entityType.isNpc) {
            (this as Npc).avatar.extendedInfo.setSequence(id, startDelay)
        } else if (entityType.isPlayer) {
            (this as Player).avatar.extendedInfo.setSequence(id, startDelay)
        }
    }

    abstract fun graphic(
        id: Int,
        height: Int = 0,
        delay: Int = 0,
    )

    fun applyTint(
        hue: Int = 0,
        saturation: Int = 0,
        luminance: Int = 0,
        opacity: Int = 0,
        delay: Int = 0,
        duration: Int = 0,
    ) {
        if (entityType.isNpc) {
            (this as Npc).avatar.extendedInfo.tinting(
                startTime = delay,
                endTime = duration,
                hue = hue,
                saturation = saturation,
                lightness = luminance,
                weight = opacity,
            )
        } else if (entityType.isPlayer) {
            (this as Player).avatar.extendedInfo.tinting(
                startTime = delay,
                endTime = duration,
                hue = hue,
                saturation = saturation,
                lightness = luminance,
                weight = opacity,
            )
        }
    }

    fun overrideLevel(level: Int) {
        if (entityType.isPlayer) {
            println("Can't override level for a player")
            return
        }
        (this as Npc).avatar.extendedInfo.combatLevelChange(level)
    }

    fun setTempName(name: String) {
        if (entityType.isPlayer) {
            println("TempName can't be applied to a player")
            return
        }
        (this as Npc).avatar.extendedInfo.nameChange(name)
    }

    fun graphic(graphic: Graphic) {
        graphic(graphic.id, graphic.height, graphic.delay)
    }

    fun forceChat(message: String) {
        if (entityType.isNpc) {
            (this as Npc).avatar.extendedInfo.setSay(message)
        } else if (entityType.isPlayer) {
            (this as Player).avatar.extendedInfo.setSay(message)
        }
    }

    fun faceDirection(direction: Direction) {
        faceTile(Tile(direction.getDeltaX(), direction.getDeltaZ()))
    }

    fun faceTile(
        face: Tile,
        width: Int = 1,
        length: Int = 1,
        instant: Int = 0,
    ) {
        if (entityType.isPlayer) {
            val srcX = tile.x * 64
            val srcZ = tile.z * 64
            val dstX = face.x * 64
            val dstZ = face.z * 64

            var degreesX = (srcX - dstX).toDouble()
            var degreesZ = (srcZ - dstZ).toDouble()

            degreesX += (Math.floor(width / 2.0)) * 32
            degreesZ += (Math.floor(length / 2.0)) * 32

            (this as Player).avatar.extendedInfo.setFaceAngle((Math.atan2(degreesX, degreesZ) * 325.949).toInt() and 0x7ff)
        } else if (entityType.isNpc) {
            // TODO we shouldnt need because we use absolute coords ADVO
            val faceX = (face.x shl 1) + 1
            val faceZ = (face.z shl 1) + 1

            (this as Npc).avatar.extendedInfo.faceCoord(face.x, face.z)
        }
    }

    fun facePawn(pawn: Pawn) {
        val index = if (pawn.entityType.isPlayer) pawn.index + 65536 else pawn.index
        if (entityType.isNpc) {
            (this as Npc).avatar.extendedInfo.setFacePathingEntity(index)
        } else if (entityType.isPlayer) {
            (this as Player).avatar.extendedInfo.setFacePathingEntity(index)
        }

        attr[FACING_PAWN_ATTR] = WeakReference(pawn)
    }

    fun resetFacePawn() {
        if (entityType.isNpc) {
            (this as Npc).avatar.extendedInfo.setFacePathingEntity(-1)
        } else if (entityType.isPlayer) {
            (this as Player).avatar.extendedInfo.setFacePathingEntity(-1)
        }

        attr.remove(FACING_PAWN_ATTR)
    }

    /**
     * Resets any interaction this pawn had with another pawn.
     */
    fun resetInteractions() {
        attr.remove(COMBAT_TARGET_FOCUS_ATTR)
        attr.remove(INTERACTING_NPC_ATTR)
        attr.remove(INTERACTING_PLAYER_ATTR)
        resetFacePawn()
    }

    fun queue(
        priority: TaskPriority = TaskPriority.STANDARD,
        logic: suspend QueueTask.(CoroutineScope) -> Unit,
    ) {
        queues.queue(this, world.coroutineDispatcher, priority, logic)
    }

    /**
     * Terminates any on-going [QueueTask]s that are being executed by this [Pawn].
     */
    fun interruptQueues() {
        queues.terminateTasks()
    }

    /**
     * Executes a plugin with this [Pawn] as its context.
     */
    fun <T> executePlugin(logic: Plugin.() -> T): T {
        val plugin = Plugin(this)
        return logic(plugin)
    }

    fun triggerEvent(event: Event) {
        world.plugins.executeEvent(this, event)
        world.getService(LoggerService::class.java, searchSubclasses = true)?.logEvent(this, event)
    }

    internal fun createPathFindingStrategy(copyChunks: Boolean = false): PathFindingStrategy {
        val collision: CollisionManager =
            if (copyChunks) {
                val chunks = world.chunks.copyChunksWithinRadius(tile.chunkCoords, height = tile.height, radius = Chunk.CHUNK_VIEW_RADIUS)
                CollisionManager(chunks, createChunksIfNeeded = false)
            } else {
                world.collision
            }
        return if (entityType.isPlayer) BFSPathFindingStrategy(collision) else SimplePathFindingStrategy(collision)
    }

    companion object {
        private val EMPTY_TILE_DEQUE = ArrayDeque<Tile>()
    }

    /*
just
    if (newAnim.id == -1 ||
        previouslySetAnim == -1 ||
        newAnim.config.priority >= config<AnimationConfig>(previouslySetAnim).priority
    )

if this is true, set the new anim and update the "previouslySetAnim" id to the newAnim.id
end of tick, reset previouslySetAnim to -1
     */
    var previouslySetAnim = -1
}
