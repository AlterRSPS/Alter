package org.alter.game.model.entity

import dev.openrune.cache.CacheManager.getNpc
import dev.openrune.cache.CacheManager.getVarbit
import dev.openrune.cache.filestore.definition.data.NpcType
import gg.rsmod.util.toStringHelper
import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcAvatar
import org.alter.game.info.NpcInfo
import org.alter.game.model.EntityType
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.CombatClass
import org.alter.game.model.combat.CombatStyle
import org.alter.game.model.combat.NpcCombatDef

/**
 * @author Tom <rspsmods@gmail.com>
 */
class Npc private constructor(val id: Int, world: World, val spawnTile: Tile) : Pawn(world) {
    constructor(id: Int, tile: Tile, world: World) : this(id, world, spawnTile = tile) {
        this.tile = tile
    }

    constructor(owner: Player, id: Int, tile: Tile, world: World) : this(id, world, spawnTile = tile) {
        this.tile = tile
        this.owner = owner
    }

    internal lateinit var avatar: NpcAvatar
    fun initAvatar(avatar: NpcAvatar) {
        check(!this::avatar.isInitialized) {"Avatar was already initialized."}
        this.avatar = avatar
    }


    /**
     * This flag indicates whether or not this npc's AI should be processed.
     *
     * As there's a small chance that most npcs will be in the viewport of a real
     * player, we don't really need to process a lot of the logic that comes with
     * the AI.
     */
    private var active = false

    /**
     * The owner of an npc will be the only [Player] who can view this npc.
     * If the owner is no longer online, this npc will be removed from the world.
     *
     * @see [org.alter.game.task.WorldRemoveTask]
     */
    var owner: Player? = null

    /**
     * This flag indicates whether or not this npc will respawn after death.
     */
    var respawns = false

    /**
     * The radius from [spawnTile], in tiles, which the npc can randomly walk.
     */
    var walkRadius = 0

    /**
     * This flag indicates whether this npc can traverse water tiles.
     */
    var canSwim = false

    /**
     * The current hitpoints the npc has.
     */
    private var hitpoints = 10

    /**
     * The [NpcCombatDef] assigned to our npc. This can change at any point to
     * another combat definition, for example if we want to transmogify the npc,
     * it may want to use a different [NpcCombatDef].
     */
    lateinit var combatDef: NpcCombatDef

    /**
     * The [CombatClass] the npc will use on its next attack.
     */
    var combatClass = CombatClass.MELEE

    /**
     * The [AttackStyle] the npc will use on its next attack.
     */
    var attackStyle = AttackStyle.CONTROLLED

    /**
     * The [CombatStyle] the npc will use on its next attack.
     */
    var combatStyle = CombatStyle.STAB

    /**
     * The [Stats] for this npc.
     */
    var stats = Stats(world.gameContext.npcStatCount)

    /**
     * Check if the npc will be aggressive towards the parameter player.
     */
    var aggroCheck: ((Npc, Player) -> Boolean)? = null

    /**
     * Gets the [NpcType] corresponding to our [id].
     */
    val def: NpcType = getNpc(id)

    /**
     * Set which routefinder logic to use.
     * 0 = Dumb route finder.
     * 1 = Smart route finder.
     */
    var routeLogic = 0

    /**
     * Getter property for our npc name.
     */
    val name: String
        get() = def.name

    /**
     * Getter property for a set of any species that our npc may be categorised
     * as.
     */
    val species: Set<Any>
        get() = combatDef.species

    override val entityType: EntityType = EntityType.NPC

    override fun isRunning(): Boolean = false

    override fun getSize(): Int = getNpc(id).size // TODO ADVO can we just do def.size() ?

    override fun getCurrentHp(): Int = hitpoints

    override fun getMaxHp(): Int = combatDef.hitpoints

    override fun setCurrentHp(health: Int) {
        this.hitpoints = health
    }

    override fun graphic(
        id: Int,
        height: Int,
        delay: Int,
    ) {
        NpcInfo(this).setSpotAnim(0, id, delay, height)
    }

    override fun cycle() {
        if (timers.isNotEmpty) {
            timerCycle()
        }
        hitsCycle()
    }

    /**
     * This method will get the "visually correct" npc id for this npc from
     * [player]'s view point.
     *
     * Npcs can change their appearance for each player depending on their
     * [NpcType.transforms] and [NpcType.transformVarp]/[NpcType.transformVarbit].
     */
    fun getTransform(player: Player): Int {
        if (def.varbit != -1) {
            val varbitDef = getVarbit(def.varbit)
            val state = player.varps.getBit(varbitDef.varp, varbitDef.startBit, varbitDef.endBit)
            return def.transforms!![state]
        }

        if (def.varp != -1) {
            val state = player.varps.getState(def.varp)
            return def.transforms!![state]
        }

        return id
    }

    /**
     * @see [Npc.active]
     */
    fun setActive(active: Boolean) {
        this.active = active
    }

    /**
     * @see [Npc.active]
     */
    fun isActive(): Boolean = active

    /**
     * Verifies if the npc is currently spawned in the world.
     */
    fun isSpawned(): Boolean = index > 0

    override fun toString(): String = toStringHelper().add("id", id).add("name", name).add("index", index).add("active", active).toString()

    companion object {
        internal const val RESET_PAWN_FACE_DELAY = 25
    }

    /**
     * @param nStats the max amount of stats an npc has.
     */
    class Stats(val nStats: Int) {
        private val currentLevels = Array(nStats) { 1 }

        private val maxLevels = Array(nStats) { 1 }

        fun getCurrentLevel(skill: Int): Int = currentLevels[skill]

        fun getMaxLevel(skill: Int): Int = maxLevels[skill]

        fun setCurrentLevel(
            skill: Int,
            level: Int,
        ) {
            currentLevels[skill] = level
        }

        fun setMaxLevel(
            skill: Int,
            level: Int,
        ) {
            maxLevels[skill] = level
        }

        /**
         * Alters the current level of the skill by adding [value] onto it.
         *
         * @param skill the skill level to alter.
         *
         * @param value the value which to add onto the current skill level.
         * This value can be negative to decrement the level.
         *
         * @param capValue the amount of levels which can be surpass the max
         * level in the skill. For example, if this value is set to [3] on a
         * skill that has is [99], that means that the level can be altered
         * from [99] to [102].
         */
        fun alterCurrentLevel(
            skill: Int,
            value: Int,
            capValue: Int = 0,
        ) {
            check(capValue == 0 || capValue < 0 && value < 0 || capValue > 0 && value >= 0) {
                "Cap value and alter value must always be the same signum (+ or -)."
            }
            val altered =
                when {
                    capValue > 0 -> Math.min(getCurrentLevel(skill) + value, getMaxLevel(skill) + capValue)
                    capValue < 0 -> Math.max(getCurrentLevel(skill) + value, getMaxLevel(skill) + capValue)
                    else -> Math.min(getMaxLevel(skill), getCurrentLevel(skill) + value)
                }
            val newLevel = Math.max(0, altered)
            val curLevel = getCurrentLevel(skill)

            if (newLevel != curLevel) {
                setCurrentLevel(skill = skill, level = newLevel)
            }
        }

        /**
         * Decrease the level of [skill].
         *
         * @param skill the skill level to alter.
         *
         * @param value the amount of levels which to decrease from [skill], as a
         * positive number.
         *
         * @param capped if true, the [skill] level cannot decrease further than
         * [getMaxLevel] - [value].
         */
        fun decrementCurrentLevel(
            skill: Int,
            value: Int,
            capped: Boolean,
        ) = alterCurrentLevel(skill, -value, if (capped) -value else 0)

        /**
         * Increase the level of [skill].
         *
         * @param skill the skill level to alter.
         *
         * @param value the amount of levels which to increase from [skill], as a
         * positive number.
         *
         * @param capped if true, the [skill] level cannot increase further than
         * [getMaxLevel].
         */
        fun incrementCurrentLevel(
            skill: Int,
            value: Int,
            capped: Boolean,
        ) = alterCurrentLevel(skill, value, if (capped) 0 else value)

        companion object {
            /**
             * The default count of stats for npcs.
             */
            const val DEFAULT_NPC_STAT_COUNT = 5
        }
    }
}
