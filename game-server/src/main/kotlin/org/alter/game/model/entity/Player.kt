package org.alter.game.model.entity

import dev.openrune.cache.CacheManager
import dev.openrune.cache.CacheManager.varpSize
import gg.rsmod.util.toStringHelper
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import net.rsprot.protocol.api.Session
import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcInfo
import net.rsprot.protocol.game.outgoing.info.playerinfo.PlayerAvatar
import net.rsprot.protocol.game.outgoing.info.playerinfo.PlayerAvatarExtendedInfo
import net.rsprot.protocol.game.outgoing.info.playerinfo.PlayerInfo
import net.rsprot.protocol.game.outgoing.info.util.BuildArea
import net.rsprot.protocol.game.outgoing.info.worldentityinfo.WorldEntityInfo
import net.rsprot.protocol.game.outgoing.inv.UpdateInvFull
import net.rsprot.protocol.game.outgoing.map.RebuildLogin
import net.rsprot.protocol.game.outgoing.misc.client.ServerTickEnd
import net.rsprot.protocol.game.outgoing.misc.client.UpdateRebootTimer
import net.rsprot.protocol.game.outgoing.misc.player.MessageGame
import net.rsprot.protocol.game.outgoing.misc.player.UpdateRunWeight
import net.rsprot.protocol.game.outgoing.misc.player.UpdateStat
import net.rsprot.protocol.game.outgoing.sound.SynthSound
import net.rsprot.protocol.game.outgoing.varp.VarpLarge
import net.rsprot.protocol.game.outgoing.varp.VarpSmall
import net.rsprot.protocol.message.OutgoingGameMessage
import org.alter.game.model.*
import org.alter.game.model.appearance.newPlayerInfo.Appearance
import org.alter.game.model.attr.CURRENT_SHOP_ATTR
import org.alter.game.model.attr.LEVEL_UP_INCREMENT
import org.alter.game.model.attr.LEVEL_UP_OLD_XP
import org.alter.game.model.attr.LEVEL_UP_SKILL_ID
import org.alter.game.model.container.ItemContainer
import org.alter.game.model.container.key.*
import org.alter.game.model.interf.InterfaceSet
import org.alter.game.model.interf.listener.PlayerInterfaceListener
import org.alter.game.model.item.Item
import org.alter.game.model.move.MovementQueue
import org.alter.game.model.move.MovementType
import org.alter.game.model.move.moveTo
import org.alter.game.model.priv.Privilege
import org.alter.game.model.queue.QueueTask
import org.alter.game.model.skill.SkillSet
import org.alter.game.model.social.Social
import org.alter.game.model.timer.ACTIVE_COMBAT_TIMER
import org.alter.game.model.timer.FORCE_DISCONNECTION_TIMER
import org.alter.game.model.varp.VarpSet
import org.alter.game.rsprot.RsModObjectProvider
import org.alter.game.service.log.LoggerService
import org.alter.game.type.bas.BasType
import org.alter.game.type.interfacedsl.Interface
import java.util.*

/**
 * A [Pawn] that represents a player.
 *
 * @author Tom <rspsmods@gmail.com>
 */
open class Player(world: World) : Pawn(world) {
    /**
     * A persistent and unique id. This is <strong>not</strong> the index
     * of our [Player] when registered to the [World], it is a value determined
     * when the [Player] first registers their account.
     */
    lateinit var uid: PlayerUID
    var displayName = ""
    var privilege = Privilege.DEFAULT
    var lastKnownRegionBase: Coordinate? = null
    var initiated = false
    var lastIndex = -1
    @Volatile private var pendingLogout = false
    var appearance: Appearance = Appearance()
    fun getPendingLogout() = pendingLogout
    @Volatile private var setDisconnectionTimer = false
    val inventory = ItemContainer(INVENTORY_KEY)
    val equipment = ItemContainer(EQUIPMENT_KEY)
    val bank = ItemContainer(BANK_KEY)

    var activeInterface = mutableListOf<Interface>()
    val interfaces by lazy { InterfaceSet(PlayerInterfaceListener(this, world.plugins)) }



    val containers =
        HashMap<ContainerKey, ItemContainer>().apply {
            put(INVENTORY_KEY, inventory)
            put(EQUIPMENT_KEY, equipment)
            put(BANK_KEY, bank)
        }
    val varps = VarpSet(maxVarps = varpSize())
    private val skillSet = SkillSet(maxSkills = world.gameContext.skillCount)
    val options = Array<String?>(10) { null }
    /**
     * TODO Why is this within Player class and not Shop itself?
     */
    var shopDirty = false
    private var largeViewport = false
    var weight = 0.0
    var skullIcon: Int? by appearance::skullIcon
    var overheadIcon: Int? by appearance::overheadIcon
    var runEnergy = 10000.00 // 100.0
    val combatLevel: Int by appearance::combatLevel
    var bas: BasType? by appearance::bas
    var gameMode = 0
    var xpRate = 1.0
    var lastMapBuildTime = 0
    val avatar: PlayerAvatar get() = playerInfo.avatar
    private val playerExtendedInfo: PlayerAvatarExtendedInfo
        get() = playerInfo.avatar.extendedInfo

    fun getSkills(): SkillSet = skillSet
    override val entityType: EntityType = EntityType.PLAYER
    /**
     * Checks if the player is running. We assume that the varp with id of
     * [173] is the running state varp.
     */
    override fun isRunning(): Boolean = varps[173].state != 0 || movementQueue.peekLastStep()?.type == MovementQueue.StepType.FORCED_RUN
    override fun getSize(): Int = 1
    override fun getCurrentHp(): Int = getSkills().getCurrentLevel(3)
    override fun getMaxHp(): Int = getSkills().getBaseLevel(3)
    override fun setCurrentHp(level: Int) {
        getSkills().setCurrentLevel(3, level)
    }
    // @TODO Pending Spot Anim array and execute on Cycle ->
    override fun graphic(
        id: Int,
        height: Int,
        delay: Int,
    ) {
        avatar.extendedInfo.setSpotAnim(0, id, delay, height)
    }

    fun forceMove(movement: ForcedMovement) {
        avatar.extendedInfo.setExactMove(
            deltaX1 = movement.diffX1,
            deltaZ1 = movement.diffZ1,
            delay1 = movement.clientDuration1,
            deltaX2 = movement.diffX2,
            deltaZ2 = movement.diffZ2,
            delay2 = movement.clientDuration2,
            angle = movement.directionAngle,
        )
    }

    suspend fun forceMove(
        task: QueueTask,
        movement: ForcedMovement,
        cycleDuration: Int = movement.maxDuration / 30,
    ) {
        movementQueue.clear()
        lock = LockState.DELAY_ACTIONS

        lastTile = tile
        moveTo(movement.finalDestination)

        forceMove(movement)

        task.wait(cycleDuration)
        lock = LockState.NONE
    }

    /**
     * Logic that should be executed every game cycle, before
     * [org.alter.game.sync.task.PlayerSynchronizationTask].
     *
     * Note that this method may be handled in parallel, so be careful with race
     * conditions if any logic may modify other [Pawn]s.
     */
    override fun cycle() {
        var calculateWeight = false
        var calculateBonuses = false

        if (pendingLogout) {
            /*
             * If a channel is suddenly inactive (disconnected), we don't to
             * immediately unregister the player. However, we do want to
             * unregister the player abruptly if a certain amount of time
             * passes since their channel disconnected.
             */
            if (setDisconnectionTimer) {
                timers[FORCE_DISCONNECTION_TIMER] = 250 // 2 mins 30 secs
                setDisconnectionTimer = false
            }

            /*
             * A player should only be unregistered from the world when they
             * do not have [ACTIVE_COMBAT_TIMER] or its cycles are <= 0, or if
             * their channel has been inactive for a while.
             *
             * We do allow players to disconnect even if they are in combat, but
             * only if the most recent damage dealt to them are by npcs.
             */
            val stopLogout =
                timers.has(
                    ACTIVE_COMBAT_TIMER,
                ) && damageMap.getAll(type = EntityType.PLAYER, timeFrameMs = 10_000).isNotEmpty()
            val forceLogout = timers.exists(FORCE_DISCONNECTION_TIMER) && !timers.has(FORCE_DISCONNECTION_TIMER)

            if (!stopLogout || forceLogout) {
                if (lock.canLogout()) {
                    handleLogout()
                    return
                }
            }
        }

        val oldRegion = lastTile?.regionId ?: -1
        if (oldRegion != tile.regionId) {
            if (oldRegion != -1) {
                world.plugins.executeRegionExit(this, oldRegion)
            }
            world.plugins.executeRegionEnter(this, tile.regionId)
        }
        if (inventory.dirty) {
            val items = inventory.rawItems
            write(
                UpdateInvFull(
                    inventoryId = 93,
                    capacity = items.size,
                    provider = RsModObjectProvider(items),
                ),
            )

            inventory.dirty = false
            calculateWeight = true
        }

        if (equipment.dirty) {
            val items = equipment.rawItems
            write(UpdateInvFull(inventoryId = 94, capacity = items.size, provider = RsModObjectProvider(items)))
            equipment.dirty = false
            calculateWeight = true
            calculateBonuses = true
            syncAppearance()
        }

        // @TODO


        //if (renderAnimationSet = CacheManager.getItem(weapon.id).renderAnimations) {
        //
        //}


        if (bank.dirty) {
            val items = bank.rawItems
            write(UpdateInvFull(inventoryId = 95, capacity = items.size, provider = RsModObjectProvider(items)))
            bank.dirty = false
        }
        if (shopDirty) {
            val shop = this.attr[CURRENT_SHOP_ATTR]
            if (shop != null) {
                    val items = shop.items.map { if (it != null) Item(it.item, it.currentAmount) else null }.toTypedArray()
                    write(UpdateInvFull(
                        inventoryId = 3,
                        capacity = items.size,
                        provider = RsModObjectProvider(items)
                    ))
            }
            shopDirty = false
        }

        if (calculateWeight) {
            calculateWeight()
        }

        if (calculateBonuses) {
            calculateBonuses()
        }

        if (timers.isNotEmpty) {
            timerCycle()
        }

        hitsCycle()

        for (i in 0 until varps.maxVarps) {
            if (varps.isDirty(i)) {
                val varp = varps[i]
                val message =
                    when {
                        varp.state in -Byte.MAX_VALUE..Byte.MAX_VALUE -> VarpSmall(varp.id, varp.state)
                        else -> VarpLarge(varp.id, varp.state)
                    }
                write(message)
            }
        }
        varps.clean()

        for (i in 0 until getSkills().maxSkills) {
            if (getSkills().isDirty(i)) {
                write(
                    UpdateStat(
                        stat = i,
                        currentLevel = getSkills().getCurrentLevel(i),
                        invisibleBoostedLevel = getSkills().getCurrentLevel(i),
                        experience = getSkills().getCurrentXp(i).toInt(),
                    ),
                )
                getSkills().clean(i)
            }
        }
    }

    /**
     * Logic that should be executed every game cycle, after updating occurs.
     */
    fun postCycle() {
        val oldTile = this.lastTile
        val moved = oldTile == null || !oldTile.sameAs(this.tile)
        val changedHeight = oldTile?.height != this.tile.height

        if (moved) {
            this.lastTile = this.tile
        }
        this.moved = false

        if (moved) {
            val oldChunk = if (oldTile != null) this.world.chunks.get(oldTile.chunkCoords, createIfNeeded = false) else null
            val newChunk = this.world.chunks.get(this.tile.chunkCoords, createIfNeeded = false)
            if (newChunk != null && (oldChunk != newChunk || changedHeight)) {
                val newSurroundings = newChunk.coords.getSurroundingCoords()
                if (!changedHeight) {
                    val oldSurroundings = oldChunk?.coords?.getSurroundingCoords() ?: ObjectOpenHashSet()
                    newSurroundings.removeAll(oldSurroundings)
                }

                newSurroundings.forEach { coords ->
                    val chunk = this.world.chunks.get(coords, createIfNeeded = true) ?: return@forEach
                    chunk.sendUpdates(this)
                    chunk.zonePartialEnclosedCacheBuffer.releaseBuffers()
                }
                if (!changedHeight) {
                    if (oldChunk != null) {
                        this.world.plugins.executeChunkExit(this, oldChunk.hashCode())
                    }
                    this.world.plugins.executeChunkEnter(this, newChunk.hashCode())
                }
            }
        }
        previouslySetAnim = -1
        write(ServerTickEnd)
        /*
         * Flush the channel at the end.
         */
        channelFlush()
    }

    /**
     * Registers this player to the [world].
     */
    fun register(): Boolean = world.register(this)

    /**
     * @TODO
     * If im not mistaking the [npcInfo] shit should be pulled out and placed into it's own class and update should happend when Player enters region
     */
    lateinit var playerInfo: PlayerInfo
    lateinit var npcInfo: NpcInfo
    lateinit var worldEntityInfo: WorldEntityInfo
    var session: Session<Client>? = null
    var buildArea: BuildArea = BuildArea.INVALID
    /**
     * Handles any logic that should be executed upon log in.
     */
    fun login() {
        playerInfo.updateCoord(tile.height, tile.x, tile.z)
        npcInfo.updateCoord(-1, tile.height, tile.x, tile.z)
        worldEntityInfo.updateCoord(-1, tile.height, tile.x, tile.z)

        if (entityType.isHumanControlled) {
            write(RebuildLogin(tile.x ushr 3, tile.z shr 3, -1, world.xteaKeyService!!, playerInfo))
            buildArea =
                BuildArea((tile.x ushr 3) - 6, (tile.z ushr 3) - 6).apply {
                    playerInfo.updateBuildArea(-1, this)
                    npcInfo.updateBuildArea(-1, this)
                    worldEntityInfo.updateBuildArea(this)
                }
            world.getService(LoggerService::class.java, searchSubclasses = true)?.logLogin(this)
        }
        if (world.rebootTimer != -1) {
            write(UpdateRebootTimer(world.rebootTimer))
        }
        syncAppearance()
        initiated = true
        world.plugins.executeLogin(this)
        social.updateStatus(this)
    }

    /**
     * Requests for this player to log out. However, the player may not be able
     * to log out immediately under certain circumstances.
     */
    fun requestLogout() {
        pendingLogout = true
        setDisconnectionTimer = true
    }

    /**
     * Handles the logic that must be executed once a player has successfully
     * logged out. This means all the prerequisites have been met for the player
     * to log out of the [world].
     *
     * The [Client] implementation overrides this method and will handle saving
     * data for the player and call this super method at the end.
     */
    internal open fun handleLogout() {
        interruptQueues()
        world.instanceAllocator.logout(this)
        world.plugins.executeLogout(this)
        world.unregister(this)
        social.updateStatus(this)
    }

    fun calculateWeight() {
        val inventoryWeight = inventory.filterNotNull().sumOf { it.getDef().weight }
        val equipmentWeight = equipment.filterNotNull().sumOf { it.getDef().weight }
        this.weight = inventoryWeight + equipmentWeight
        write(UpdateRunWeight(this.weight.toInt()))
    }

    fun calculateBonuses() {
        Arrays.fill(equipmentBonuses, 0)
        for (i in 0 until equipment.capacity) {
            val item = equipment[i] ?: continue
            val def = item.getDef()
            def.bonuses.forEachIndexed { index, bonus -> equipmentBonuses[index] += bonus }
        }
    }

    fun addXp(
        skill: Int,
        xp: Double,
    ) {
        val oldXp = getSkills().getCurrentXp(skill)
        if (oldXp >= SkillSet.MAX_XP) {
            return
        }
        val newXp = Math.min(SkillSet.MAX_XP.toDouble(), (oldXp + (xp * xpRate)))
        /*
         * Amount of levels that have increased with the addition of [xp].
         */
        val increment = SkillSet.getLevelForXp(newXp) - SkillSet.getLevelForXp(oldXp)

        /*
         * Only increment the 'current' level if it's set at its capped level.
         */
        if (getSkills().getCurrentLevel(skill) == getSkills().getBaseLevel(skill)) {
            getSkills().setBaseXp(skill, newXp)
        } else {
            getSkills().setXp(skill, newXp)
        }

        if (increment > 0) {
            attr[LEVEL_UP_SKILL_ID] = skill
            attr[LEVEL_UP_INCREMENT] = increment
            attr[LEVEL_UP_OLD_XP] = oldXp
            world.plugins.executeSkillLevelUp(this)
        }
    }

    fun syncAppearance() {
        if (!appearance.rebuild) {
            return
        }
        val info = playerExtendedInfo
        val colours = appearance.coloursSnapshot()
        for (i in colours.indices) {
            info.setColour(i, colours[i].toInt())
        }
        val identKit = appearance.identKitSnapshot()
        for (i in identKit.indices) {
            info.setIdentKit(i, identKit[i].toInt())
        }
        info.setName(displayName)
        info.setOverheadIcon(overheadIcon ?: -1)
        info.setSkullIcon(skullIcon ?: -1)
        info.setCombatLevel(combatLevel)
        info.setBodyType(appearance.bodyType)
        info.setPronoun(appearance.pronoun)
        info.setHidden(appearance.softHidden)
        info.setNameExtras(
            beforeName = appearance.namePrefix ?: "",
            afterName = appearance.nameSuffix ?: "",
            afterCombatLevel = appearance.combatLvlSuffix ?: "",
        )
        /**
         * TODO Assign default
         */
        val bas = this.appearance.bas ?: BasType(
            name = "bas.default_player",
            walkAnim = 819,
            runAnim = 824,
            readyAnim = 808,
            turnAnim = 823,
            walkAnimBack = 820,
            walkAnimLeft = 821,
            walkAnimRight = 822,
            accurateAnim = 422,
            accurateSound = 2_566,
            aggressiveAnim = 423,
            aggressiveSound = 2_566,
            controlledAnim = -1,
            controlledSound = -1,
            defensiveAnim = 422,
            defensiveSound = 2_566,
            blockAnim = 424
        )
        info.setBaseAnimationSet(
            readyAnim = bas.readyAnim,
            turnAnim = bas.turnAnim,
            walkAnim = bas.walkAnim,
            walkAnimBack = bas.walkAnimBack,
            walkAnimLeft = bas.walkAnimLeft,
            walkAnimRight = bas.walkAnimRight,
            runAnim = bas.runAnim,
        )
        for (i in 0 until 12) {
            val obj = equipment[i]
            if (obj == null) {
                info.setWornObj(i, -1, -1, -1)
            } else {
                val config = CacheManager.getItem(obj.id)
                info.setWornObj(i, obj.id, config.appearanceOverride1, config.appearanceOverride2)
            }
        }
    }

    fun setSequence(id: Int, delay: Int) {
        playerExtendedInfo.setSequence(id, delay)
    }

    fun tinting(hue: Int = 0,
                saturation: Int = 0,
                luminance: Int = 0,
                opacity: Int = 0,
                delay: Int = 0,
                duration: Int = 0) {
        playerExtendedInfo.setTinting(hue, saturation, luminance, opacity, delay, duration)
    }

    fun setSay(message:String) {
        playerExtendedInfo.setSay(message)
    }
    fun setFaceCoord(
        face: Tile,
        width: Int = 1,
        length: Int = 1,
    ) {
        val srcX = tile.x * 64
        val srcZ = tile.z * 64
        val dstX = face.x * 64
        val dstZ = face.z * 64
        var degreesX = (srcX - dstX).toDouble()
        var degreesZ = (srcZ - dstZ).toDouble()
        degreesX += (Math.floor(width / 2.0)) * 32
        degreesZ += (Math.floor(length / 2.0)) * 32
        playerExtendedInfo.setFaceAngle((Math.atan2(degreesX, degreesZ) * 325.949).toInt() and 0x7ff)
    }
    fun facePawn(index: Int) {
        playerExtendedInfo.setFacePathingEntity(index)
    }
    fun setMoveSpeed(movementType: MovementType) {
        playerExtendedInfo.setMoveSpeed(movementType.value)
    }


    /**
     * @see largeViewport
     */
    fun setLargeViewport(largeViewport: Boolean) {
        this.largeViewport = largeViewport
    }

    /**
     * @see largeViewport
     */
    fun hasLargeViewport(): Boolean = largeViewport

    /**
     * Invoked when the player should close their current interface modal.
     */
    internal fun closeInterfaceModal() {
        world.plugins.executeModalClose(this)
    }

    /**
     * Checks if the player is registered to a [PawnList] as they should be
     * solely responsible for write access on the index. Being registered
     * to the list should essentially mean the player is registered to the
     * [world].
     *
     * @return
     * true if the player is registered to a [PawnList].
     */
    val isOnline: Boolean get() = index > 0

    /**
     * Default method to handle any incoming [Message]s that won't be
     * handled unless the [Player] is controlled by a [Client] user.
     */
    open fun handleMessages() {
    }

    /**
     * Default method to write [Message]s to the attached channel that won't
     * be handled unless the [Player] is controlled by a [Client] user.
     */
    open fun write(vararg messages: OutgoingGameMessage) {
    }

    open fun write(vararg messages: Any) {
    }

    /**
     * Default method to flush the attached channel. Won't be handled unless
     * the [Player] is controlled by a [Client] user.
     */
    open fun channelFlush() {
    }

    /**
     * Default method to close the attached channel. Won't be handled unless
     * the [Player] is controlled by a [Client] user.
     */
    open fun channelClose() {
    }

    /**
     * Write a [MessageGameMessage] to the client.
     */
    internal fun writeMessage(message: String) {
        write(MessageGame(type = 0, message = message))
    }

    internal fun playSound(
        id: Int,
        volume: Int = 1,
        delay: Int = 0,
    ) {
        write(SynthSound(id = id, loops = volume, delay = delay))
    }




    override fun toString(): String =
        toStringHelper()
            .add("name", displayName)
            .add("pid", index)
            .toString()




















    companion object {
        /**
         * How many tiles a player can 'see' at a time, normally.
         */
        const val NORMAL_VIEW_DISTANCE = 15

        /**
         * How many tiles a player can 'see' at a time when in a 'large' viewport.
         */
        const val LARGE_VIEW_DISTANCE = 127

        /**
         * How many tiles in each direction a player can see at a given time.
         * This should be as far as players can see entities such as ground items
         * and objects.
         */
        const val TILE_VIEW_DISTANCE = 32
    }

    var social = Social()
}
