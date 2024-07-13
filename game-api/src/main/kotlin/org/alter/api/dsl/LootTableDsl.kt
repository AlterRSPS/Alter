package org.alter.api.dsl

import org.alter.game.model.Tile
import org.alter.api.NpcCombatBuilder
import org.alter.game.model.entity.Player
import org.alter.game.model.weightedTableBuilder.*
import java.lang.Math.random
import kotlin.random.Random
/**
 * @TODO For now it's only set up to work with NpcCombatBuilder, we want to make it work with Item Builder as well.
 * @TODO Most likely will be removed in the future.
 */
class WeightedTableBuilder {
    var combatBuilder: NpcCombatBuilder? = null
    /**
     * At what position to drop the item.
     * or if null, drop at the npcs position.
     */
    var position: Tile? = null
    /**
     * How many times to do a roll for drop, excluding the always table.
     */
    var rolls = 1
    /**
     * Collection of drop tables.
     */
    var LootTables = mutableSetOf<LootTable>()




    inner class AlwaysTableBuilder {
        fun add(item: Any?, min: Int = 1, max: Int = 1, steepness: Int = 1, description: String ? = null, announce : Boolean = false, block: (Player) -> Boolean = { true }) {
            addToTable(TableType.ALWAYS, Loot(item = item, min = min, max = max, steepness = steepness, description = description, announce = announce, block = block))
        }
        fun add(item: Int, min: Int = 1, max: Int = 1, steepness: Int = 1, description: String ? = null, announce : Boolean = false, block: (Player) -> Boolean = { true }) {
            addToTable(TableType.ALWAYS, Loot(item = item, min = min, max = max, steepness = steepness, description = description, announce = announce, block = block))
        }
        fun add(item: Int, amount: Int, description: String ? = null, announce : Boolean = false, block: (Player) -> Boolean = { true }) {
            addToTable(TableType.ALWAYS, Loot(item = item, min = amount, max = amount, steepness = 1, description = description, announce = announce, block = block))
        }
    }
    fun always(init: AlwaysTableBuilder.() -> Unit) {
        val builder = AlwaysTableBuilder()
        builder.init()
    }
    inner class MainTableBuilder(val weight: Int = 0) {
        fun add(item: Any?, min: Int = 1, max: Int = 1, steepness: Int = 1, description: String ? = null, announce : Boolean = false, block: (Player) -> Boolean = { true }) {
            addToTable(TableType.MAIN, weight = this.weight, loot = Loot(item = item, min = min, max = max, steepness = steepness, description = description, announce = announce, block = block))
        }
        fun add(item: Any?, amount: Int, description: String ? = null, announce : Boolean = false, block: (Player) -> Boolean = { true }) {
            addToTable(TableType.MAIN, weight = this.weight, loot = Loot(item = item, min = amount, max = amount, steepness = 1, description = description, announce = announce, block = block))
        }
        fun add(item: Any?, min: Int = 1, max: Int = 1, steepness: Int = 1, weight: Int = 0,description: String ? = null, announce : Boolean = false, block: (Player) -> Boolean = { true }) {
            addToTable(TableType.MAIN, weight = this.weight, loot = Loot(item = item, min = min, max = max, steepness = steepness, weight = weight, description = description, announce = announce, block = block))
        }
        fun add(item: Any?, amount: Int, weight: Int = 0, description: String ? = null, announce : Boolean = false, block: (Player) -> Boolean = { true }) {
            addToTable(TableType.MAIN, weight = this.weight, loot = Loot(item = item, min = amount, max = amount, steepness = 1, weight = weight, description = description, announce = announce, block = block))
        }
    }
    fun main(weight: Int = 0 ,init: MainTableBuilder.() -> Unit) {
        val builder = MainTableBuilder(weight = weight)
        builder.init()
    }
    inner class preroll {
        fun add(item: Any?, min: Int = 1, max: Int = 1, steepness: Int = 1, weight: Int = 0, description: String ? = null, announce : Boolean = false, block: (Player) -> Boolean = { true }) {
            addToTable(TableType.PRE_ROLL, loot = Loot(item = item, min = min, max = max, steepness = steepness, weight = weight, description = description, announce = announce, block = block))
        }
        fun add(item: Any?, amount: Int, weight: Int = 0, description: String ? = null, announce : Boolean = false, block: (Player) -> Boolean = { true }) {
            addToTable(TableType.PRE_ROLL, loot = Loot(item = item, min = amount, max = amount, steepness = 1, weight = weight, description = description, announce = announce, block = block))
        }
    }
    fun preroll(init: preroll.() -> Unit) {
        val builder = preroll()
        builder.init()
    }
    inner class tertiary(val weight: Int = 0) {
        fun add(item: Any?, min: Int = 1, weight: Int = 0, max: Int = 1, steepness: Int = 1, description: String ? = null, announce : Boolean = false, block: (Player) -> Boolean = { true }) {
            addToTable(TableType.TERTIARY, weight = this.weight, loot = Loot(item = item, min = min, max = max, steepness = steepness, weight = weight, description = description, announce = announce, block = block))
        }
        fun add(item: Any?, amount: Int, weight: Int = 0, description: String ? = null, announce : Boolean = false, block: (Player) -> Boolean = { true }) {
            addToTable(TableType.TERTIARY, weight = this.weight, loot = Loot(item = item, min = amount, max = amount, steepness = 1, weight = weight, description = description, announce = announce, block = block))
        }
    }
    fun tertiary(weight: Int = 0, init: tertiary.() -> Unit) {
        val builder = tertiary(weight = weight)
        builder.init()
    }

    /**
     * @TODO Cleanup :kappa:
     */
    private fun addToTable(table: TableType, loot: Loot, weight: Int = 0) {
        if (LootTables.firstOrNull { it.tableType == table } != null) {
            LootTables.firstOrNull { it.tableType == table }!!.drops.add(loot)
        } else {
            LootTables.add(LootTable(
                tableType = table,
                tableWeight = weight,
                dropPosition = position,
                rolls = rolls,
                drops = mutableSetOf(loot)
            ))
        }
        combatBuilder.let {
            combatBuilder?.LootTable = LootTables
        }
    }


}
