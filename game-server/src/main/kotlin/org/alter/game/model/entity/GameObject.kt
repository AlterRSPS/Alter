package org.alter.game.model.entity

import dev.openrune.cache.CacheManager
import dev.openrune.cache.CacheManager.getObject
import dev.openrune.cache.filestore.definition.data.ObjectType
import gg.rsmod.util.toStringHelper
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.attr.AttributeMap
import org.alter.game.model.timer.TimerMap

/**
 * A [GameObject] is any type of map object that can occupy a tile.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class GameObject : Entity {
    /**
     * The object id.
     */
    val id: Int

    /**
     * A bit-packed byte that holds the object "type" and "rotation".
     */
    val settings: Byte

    /**
     * @see [AttributeMap]
     */
    val attr = AttributeMap()

    /**
     * @see [TimerMap]
     */
    val timers = TimerMap()

    /**
     * Thanks to <a href="https://www.rune-server.ee/members/maxi/">Maxi</a> for this information:
     * <a href="https://www.rune-server.ee/runescape-development/rs2-client/configuration/462827-object-types-short-definitions.html">Object types short definitions</a>
     0	- straight walls, fences etc
     1	- diagonal walls corner, fences etc connectors
     2	- entire walls, fences etc corners
     3	- straight wall corners, fences etc connectors
     4	- straight inside wall decoration
     5	- straight outside wall decoration
     6	- diagonal outside wall decoration
     7	- diagonal inside wall decoration
     8	- diagonal in wall decoration
     9	- diagonal walls, fences etc
     10	- all kinds of objects, trees, statues, signs, fountains etc etc
     11	- ground objects like daisies etc
     12	- straight sloped roofs
     13	- diagonal sloped roofs
     14	- diagonal slope connecting roofs
     15	- straight sloped corner connecting roofs
     16	- straight sloped corner roof
     17	- straight flat top roofs
     18	- straight bottom egde roofs
     19	- diagonal bottom edge connecting roofs
     20	- straight bottom edge connecting roofs
     21	- straight bottom edge connecting corner roofs
     22	- ground decoration + map signs (quests, water fountains, shops etc)
     */
    val type: Int get() = settings.toInt() shr 2

    val rot: Int get() = settings.toInt() and 3

    private constructor(id: Int, settings: Int, tile: Tile) {
        this.id = id
        this.settings = settings.toByte()
        this.tile = tile
    }

    constructor(id: Int, type: Int, rot: Int, tile: Tile) : this(id, (type shl 2) or rot, tile)

    fun getDef(): ObjectType = getObject(id)

    fun isSpawned(world: World): Boolean = world.isSpawned(this)

    /**
     * This method will get the "visually correct" object id for this npc from
     * [player]'s view point.
     *
     * Objects can change their appearance for each player depending on their
     * [ObjectType.transforms] and [ObjectType.varp]/[ObjectType.varbit].
     */
    fun getTransform(player: Player): Int {
        val world = player.world
        val def = getDef()

        if (def.varbit != -1) {
            val varbitDef = CacheManager.getVarbit(def.varbit)
            val state = player.varps.getBit(varbitDef.varp, varbitDef.startBit, varbitDef.endBit)
            return def.transforms!![state]
        }

        if (def.varp != -1) {
            val state = player.varps.getState(def.varp)
            return def.transforms!![state]
        }

        return id
    }

    override fun toString(): String =
        toStringHelper().add("id", id).add("type", type).add("rot", rot).add("tile", tile.toString()).toString()
}
