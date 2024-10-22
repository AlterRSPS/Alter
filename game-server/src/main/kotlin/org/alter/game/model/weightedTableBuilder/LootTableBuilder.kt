package org.alter.game.model.weightedTableBuilder

import org.alter.game.model.Tile
import org.alter.game.model.entity.GroundItem
import org.alter.game.model.entity.Player
import kotlin.random.Random
import kotlin.reflect.KFunction
import kotlin.reflect.typeOf

enum class TableType {
    ALWAYS,
    PRE_ROLL,
    MAIN,
    TERTIARY,
}

data class Loot(
    val item: Any?,
    val min: Int,
    val max: Int = 1,
    val steepness: Int = 1,
    val weight: Int? = 0,
    val description: String? = null,
    val announce: Boolean = false,
    val block: Player.() -> Boolean = { true }
)

/**
 * @TODO Improve these checks, so that there would not be any empty loot slots.
 */
data class LootTable(
    val tableType: TableType,
    val tableWeight: Int? = 0,
    val drops: MutableSet<Loot>
) {
    init {
        when(tableType) {
            TableType.ALWAYS -> {
                check(tableWeight != null) { "Table weight is not required for ALWAYS TABLE." }
            }
            else -> {
               //check(tableWeight == 0 || tableWeight == null) { "$tableType is fucked." }
                if (tableWeight != null) {
                    var i = 0
                    drops.forEach {
                        if (it.weight != null) {
                            i += it.weight
                        }
                    }
                    check(tableWeight >= i) { "Why be so retarded?" }
                }
            }
        }
    }
}



fun randomStep(start: Int, stop: Int, step: Int): Int {
    val result = (start..stop step step).toList()
    if (result.isEmpty()) {
        return start
    }
    return (start..stop step step).toList().random()
}
val random = Random
fun random(range: IntRange): Int = random.nextInt(range.endInclusive - range.start + 1) + range.start
fun random(boundInclusive: Int) = random.nextInt(boundInclusive + 1)
/**
 * Table rollers
 */
fun LootTable.mainRoll(): Loot {
    var total = tableWeight ?: 0 // total table weight
    val roll = random(total)
    var cur = 0
    for (loot in drops) {
        loot.weight?.let {
            cur += it // e.g. 10 for rune longsword, 9 for adamant platebody... etc
            if (cur >= roll) {
                return loot
            }
        }
    }
    // should never happen unless ur table is broken
    throw IllegalStateException("fix ur code idiot")
}
fun LootTable.preRoll(): Loot? {
    for (loot in drops) {
        loot.weight!!.let {
            if (Random.nextInt(loot.weight) == 0) {
                return loot
            }
        }
    }
    return null
}
fun LootTable.tertiaryRoll(): List<Loot> {
    val items = ArrayList<Loot>()
    for (loot in drops) {
        loot.weight!!.let {
            if (Random.nextInt(loot.weight) == 0) {
                items += loot
            }
        }
    }
    return items
}

/**
 * Shit whats now bad how will you invoke [item.block] <-- Too which we should pass if reroll should happen.
 * This param should be only set to true when we want to reroll for drop.
 */
val reroll: Boolean = false

fun Loot.handleToItem(p: Player) : List<GroundItem> {
    val items = mutableListOf<GroundItem>()
    if (block.invoke(p)) {
        val tile = Tile(0, 0, 0) // Default value to send in for Tile
        when (item) {
            is Int -> {
                items.add(GroundItem(item, amount = randomStep(min, max, steepness), tile = tile))
            }
            is LootTable -> {
                items.addAll(roll(p, setOf(item)))
            }
            is KFunction<*> -> {
                try {
                    if (item.returnType == typeOf<LootTable>()) {
                        val result = item.call() as LootTable
                        result.drops.forEach {
                            items.addAll(it.handleToItem(p))
                        }
                    } else if (item.returnType == typeOf<Loot>()) {
                        val result = item.call() as Loot
                        items.addAll(result.handleToItem(p))
                    } else {
                        throw IllegalStateException("Unhandled LootTable return type: ${item.returnType}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            else -> throw IllegalStateException("Unhandled drop type: ${item?.javaClass}")
        }
    }
    return items
}
fun roll(p: Player, lootTables: Set<LootTable>?):Set<GroundItem>  {
    val dropSet = mutableSetOf<GroundItem?>()
    val tables = TableType.values().associateWith { tableType ->
        lootTables?.filter { it.tableType == tableType }
    }
    tables[TableType.ALWAYS]?.forEach {
        it.drops.forEach {
            it.handleToItem(p).forEach {
                dropSet.add(it)
            }
        }
    }
    tables[TableType.TERTIARY]?.firstOrNull()?.tertiaryRoll()?.let {
        it.forEach {
            it.handleToItem(p).forEach {
                dropSet.add(it)
            }
        }
    }
    tables[TableType.PRE_ROLL]?.firstOrNull()?.preRoll()?.let {
        it.handleToItem(p).forEach {
            dropSet.add(it)
        }
    } ?: tables[TableType.MAIN]?.firstOrNull()?.mainRoll()?.let {
        it.handleToItem(p).forEach {
            dropSet.add(it)
        }
    }
    return dropSet.filterNotNull().toSet()
}

