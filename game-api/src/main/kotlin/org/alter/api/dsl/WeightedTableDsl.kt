package org.alter.api.dsl

import org.alter.game.model.Tile
import org.alter.game.model.weightedTableBuilder.TableTypes
import org.alter.game.model.weightedTableBuilder.itemDrop
import org.alter.api.NpcCombatBuilder
import org.alter.game.model.weightedTableBuilder.tableDrops


object WeightedTable {
    @DslMarker
    annotation class WeightedTableDslMarker
    @WeightedTableDslMarker
    class WeightedTableBuilder(val combatBuilder: NpcCombatBuilder) {


        /**
         * If position is not set, it will mean drop where the npc died.
         */
        var position: Tile = Tile(0,0)


        open class TableBuilder(val combatBuilder: NpcCombatBuilder, val type: TableTypes, val tableRolls: Int = 1, val tableWeight: Int = 0) {
            fun add(itemid: Int, min: Int = 1, max: Int = 1, weight: Int = 0, description: String? = null, block: Unit.() -> Boolean = { true }) {
                combatBuilder.addDropToTable(
                    type = type,
                    item = itemDrop(
                        itemId = itemid,
                        min = min,
                        max = max,
                        weight = weight,
                        description = description,
                        block = block
                    ),
                    tableWeight = tableWeight,
                    tableRolls = tableRolls
                )
            }
        }


        fun always(init: TableBuilder.() -> Unit) {
            val builder = TableBuilder(combatBuilder, TableTypes.ALWAYS)
            builder.init()
        }

        fun preroll(tableWeight: Int, rolls: Int = 1, init: TableBuilder.() -> Unit) {
            val builder = TableBuilder(combatBuilder, TableTypes.PRE_ROLL, tableWeight = tableWeight, tableRolls = rolls)
            builder.init()
        }
        fun main(tableWeight: Int, rolls: Int = 1, init: TableBuilder.() -> Unit) {
            val builder = TableBuilder(combatBuilder, TableTypes.MAIN, tableWeight = tableWeight)
            builder.init()
        }
    }
}

fun NpcCombatBuilder.addDropToTable(type: TableTypes, item: itemDrop, tableWeight: Int = 0, tableRolls: Int = 1) {
    if (dropTable.any { it.tableType == type }) {
        val existingTable: tableDrops? = dropTable.find { it.tableType == type }
        val updatedDrops = existingTable?.drops?.plus(item) ?: setOf(item)
        dropTable.remove(existingTable)
        dropTable.add(existingTable?.copy(drops = updatedDrops) ?: tableDrops(type, tableWeight,tableRolls, updatedDrops))
    } else {
        dropTable.add(tableDrops(type, tableWeight, tableRolls, setOf(item)))
    }
}
