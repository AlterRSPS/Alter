package org.alter.game.model.entity

import gg.rsmod.util.toStringHelper
import net.rsprot.protocol.api.Session
import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcInfo
import net.rsprot.protocol.game.outgoing.info.playerinfo.PlayerAvatar
import net.rsprot.protocol.game.outgoing.info.playerinfo.PlayerInfo
import net.rsprot.protocol.game.outgoing.inv.UpdateInvFull
import net.rsprot.protocol.game.outgoing.map.RebuildLogin
import net.rsprot.protocol.game.outgoing.misc.client.UpdateRebootTimer
import net.rsprot.protocol.game.outgoing.misc.player.MessageGame
import net.rsprot.protocol.game.outgoing.misc.player.UpdateRunWeight
import net.rsprot.protocol.game.outgoing.misc.player.UpdateStat
import net.rsprot.protocol.game.outgoing.sound.SynthSound
import net.rsprot.protocol.game.outgoing.varp.VarpLarge
import net.rsprot.protocol.game.outgoing.varp.VarpSmall
import net.rsprot.protocol.message.OutgoingGameMessage
import org.alter.game.fs.def.VarpDef
import org.alter.game.model.*
import org.alter.game.model.appearance.Appearance
import org.alter.game.model.appearance.Gender
import org.alter.game.model.attr.CURRENT_SHOP_ATTR
import org.alter.game.model.attr.LEVEL_UP_INCREMENT
import org.alter.game.model.attr.LEVEL_UP_OLD_XP
import org.alter.game.model.attr.LEVEL_UP_SKILL_ID
import org.alter.game.model.container.ItemContainer
import org.alter.game.model.container.key.*
import org.alter.game.model.interf.InterfaceSet
import org.alter.game.model.interf.listener.PlayerInterfaceListener
import org.alter.game.model.item.Item
import org.alter.game.model.priv.Privilege
import org.alter.game.model.queue.QueueTask
import org.alter.game.model.skill.SkillSet
import org.alter.game.model.social.Social
import org.alter.game.model.timer.ACTIVE_COMBAT_TIMER
import org.alter.game.model.timer.FORCE_DISCONNECTION_TIMER
import org.alter.game.model.varp.VarpSet
import org.alter.game.rsprot.RsModObjectProvider
import org.alter.game.service.log.LoggerService
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

    /**
     * The name that was used when the player logged into the game.
     */
    var username = ""

    /**
     * @see Privilege
     */
    var privilege = Privilege.DEFAULT

    /**
     * The base region [Coordinate] is the most bottom-left (south-west) tile where
     * the last known region for this player begins.
     */
    var lastKnownRegionBase: Coordinate? = null

    /**
     * A flag that indicates whether the [login] method has been executed.
     * This is currently used so that we don't send player updates when the player
     * hasn't been fully initialized. We can test later to see if this is even
     * necessary.
     */
    var initiated = false

    /**
     * The index that was assigned to a [Player] when they are first registered to the
     * [World]. This is needed to remove local players from the synchronization task
     * as once that logic is reached, the local player would have an index of [-1].
     */
    var lastIndex = -1

    /**
     * A flag which indicates the player is attempting to log out. There can be
     * certain circumstances where the player should not be unregistered from
     * the world.
     *
     * For example: when the player is in combat.
     */
    @Volatile private var pendingLogout = false

    /**
     * A flag which indicates that our [FORCE_DISCONNECTION_TIMER] must be set
     * when [pendingLogout] logic is handled.
     */
    @Volatile private var setDisconnectionTimer = false

    val bonds = ItemContainer(world.definitions, BOND_POUCH_KEY)

    val inventory = ItemContainer(world.definitions, INVENTORY_KEY)

    val equipment = ItemContainer(world.definitions, EQUIPMENT_KEY)

    val bank = ItemContainer(world.definitions, BANK_KEY)

    /**
     * A map that contains all the [ItemContainer]s a player can have.
     */
    val containers = HashMap<ContainerKey, ItemContainer>().apply {
        put(BOND_POUCH_KEY, bonds)
        put(INVENTORY_KEY, inventory)
        put(EQUIPMENT_KEY, equipment)
        put(BANK_KEY, bank)
    }

    val interfaces by lazy { InterfaceSet(PlayerInterfaceListener(this, world.plugins)) }

    val varps = VarpSet(maxVarps = world.definitions.getCount(VarpDef::class.java))

    private val skillSet = SkillSet(maxSkills = world.gameContext.skillCount)

    /**
     * The options that can be executed on this player
     */
    val options = Array<String?>(10) { null }

    /**
     * Flag that indicates whether to refresh the shop the player currently
     * has open.
     */
    var shopDirty = false

    /**
     * Some areas have a 'large' viewport. Which means the player's client is
     * able to render more entities in a larger radius than normal.
     */
    private var largeViewport = false

    var appearance = Appearance.DEFAULT_MALE

    var weight = 0.0

    var skullIcon = -1

    var runEnergy = 10000.00 // 100.0

    /**
     * The current combat level. This must be set externally by a login plugin
     * that is used on whatever revision you want.
     */
    var combatLevel = 3

    var gameMode = 0

    var xpRate = 1.0

    /**
     * The last cycle that this client has received the MAP_BUILD_COMPLETE
     * message. This value is set to [World.currentCycle].
     *
     * @see [org.alter.game.message.handler.MapBuildCompleteHandler]
     */
    var lastMapBuildTime = 0

    fun getSkills(): SkillSet = skillSet

    override val entityType: EntityType = EntityType.PLAYER

    fun setBaseAnimationSet(readyAnim: Int, turnAnim: Int, walkAnim: Int, walkAnimBack: Int,
                            walkAnimLeft: Int, walkAnimRight: Int, runAnim: Int) {
        avatar.extendedInfo.setBaseAnimationSet(
            readyAnim = readyAnim,
            turnAnim =  turnAnim,
            walkAnim = walkAnim,
            walkAnimBack = walkAnimBack,
            walkAnimLeft = walkAnimLeft,
            walkAnimRight = walkAnimRight,
            runAnim = runAnim,
        )
    }

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

    val avatar: PlayerAvatar get() = playerInfo.avatar

    override fun graphic(id: Int, height: Int, delay: Int) {
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

    suspend fun forceMove(task: QueueTask, movement: ForcedMovement, cycleDuration: Int = movement.maxDuration / 30) {
        movementQueue.clear()
        lock = LockState.DELAY_ACTIONS

        lastTile = Tile(tile)
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
            val stopLogout = timers.has(ACTIVE_COMBAT_TIMER) && damageMap.getAll(type = EntityType.PLAYER, timeFrameMs = 10_000).isNotEmpty()
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
            write(UpdateInvFull(interfaceId = 149, componentId = 0, inventoryId = 93, capacity = items.size, provider = RsModObjectProvider(items)))

            inventory.dirty = false
            calculateWeight = true
        }

        if (equipment.dirty) {
            val items = equipment.rawItems
            write(UpdateInvFull(inventoryId = 94, capacity = items.size, provider = RsModObjectProvider(items)))
            equipment.dirty = false
            calculateWeight = true
            calculateBonuses = true


            items.forEach { item ->
                //TODO ADVO THIS IS SHIT
                val def = item?.getDef(world.definitions)?: return
                avatar.extendedInfo.setWornObj(def.wearPos1, item.id, def.wearPos2, def.wearPos3)
            }
        }

        if (bank.dirty) {
            val items = bank.rawItems
            write(UpdateInvFull(inventoryId = 95, capacity = items.size, provider = RsModObjectProvider(items)))
            bank.dirty = false
        }

        if (shopDirty) {
            attr[CURRENT_SHOP_ATTR]?.let { shop -> {
                    val items = shop.items.map { if (it != null) Item(it.item, it.currentAmount) else null }.toTypedArray()
                    write(UpdateInvFull(inventoryId = 13, capacity = items.size, provider = RsModObjectProvider(items)))
                }
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
                val message = when {
                    varp.state in -Byte.MAX_VALUE..Byte.MAX_VALUE -> VarpSmall(varp.id, varp.state)
                    else -> VarpLarge(varp.id, varp.state)
                }
                write(message)
            }
        }
        varps.clean()

        for (i in 0 until getSkills().maxSkills) {
            if (getSkills().isDirty(i)) {
                write(UpdateStat(stat = i, currentLevel = getSkills().getCurrentLevel(i), invisibleBoostedLevel = getSkills().getCurrentLevel(i), experience = getSkills().getCurrentXp(i).toInt()))
                getSkills().clean(i)
            }
        }
    }

    /**
     * Logic that should be executed every game cycle, after
     * [org.alter.game.sync.task.PlayerSynchronizationTask].
     *
     * Note that this method may be handled in parallel, so be careful with race
     * conditions if any logic may modify other [Pawn]s.
     */
    fun postCycle() {
        previouslySetAnim = -1;
        /*
         * Flush the channel at the end.
         */
        channelFlush()
    }

    /**
     * Registers this player to the [world].
     */
    fun register(): Boolean = world.register(this)

    lateinit var playerInfo: PlayerInfo
    lateinit var npcInfo: NpcInfo
    var session: Session<Client>? = null


    /**
     * Handles any logic that should be executed upon log in.
     */
    fun login() {
        playerInfo.updateCoord(tile.height, tile.x, tile.z)
        if (entityType.isHumanControlled) {
            write(RebuildLogin(tile.x shr 3, tile.z shr 3, world.xteaKeyService!!, playerInfo))
            avatar.postUpdate() // TODO ADVO on newer API this isnt needed
            world.getService(LoggerService::class.java, searchSubclasses = true)?.logLogin(this)
        }

        if (world.rebootTimer != -1) {
            write(UpdateRebootTimer(world.rebootTimer))
        }
        //TODO ADVO extract this and sync appearance if dirty
        for (slot in 0..6) {
            avatar.extendedInfo.setIdentKit(slot, appearance.getLook(slot))
        }
        for (slot in 0..11) {
            avatar.extendedInfo.setWornObj(slot, -1, -1, -1)
        }
        for (slot in 0..4) {
            avatar.extendedInfo.setColour(slot, appearance.colors[slot])
        }
        avatar.extendedInfo.setBaseAnimationSet(
            readyAnim = 808,
            turnAnim =  823,
            walkAnim = 819,
            walkAnimBack = 820,
            walkAnimLeft = 821,
            walkAnimRight = 822,
            runAnim = 824,
        )
        avatar.extendedInfo.setName(username)
        avatar.extendedInfo.setCombatLevel(combatLevel)
        avatar.extendedInfo.setMale(appearance.gender == Gender.MALE)
        avatar.extendedInfo.setOverheadIcon(prayerIcon)
        avatar.extendedInfo.setSkullIcon(-1)
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
        val inventoryWeight = inventory.filterNotNull().sumOf { it.getDef(world.definitions).weight }
        val equipmentWeight = equipment.filterNotNull().sumOf { it.getDef(world.definitions).weight }
        this.weight = inventoryWeight + equipmentWeight
        write(UpdateRunWeight(this.weight.toInt()))
    }

    fun calculateBonuses() {
        Arrays.fill(equipmentBonuses, 0)
        for (i in 0 until equipment.capacity) {
            val item = equipment[i] ?: continue
            val def = item.getDef(world.definitions)
            def.bonuses.forEachIndexed { index, bonus -> equipmentBonuses[index] += bonus }
        }
    }

    fun addXp(skill: Int, xp: Double) {
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

    internal fun playSound(id: Int, volume: Int = 1, delay: Int = 0) {
        write(SynthSound(id = id, loops = volume, delay = delay))
    }

    override fun toString(): String = toStringHelper()
            .add("name", username)
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
