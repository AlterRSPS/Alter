package org.alter.game.model.weightedTableBuilder

import org.alter.game.model.Tile
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player

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

data class LootTable(
    val tableType: TableType,
    val tableWeight: Int? = 0,
    /**
     * At what position to drop the item.
     * or if null, drop at the npcs position.
     */
    val dropPosition: Tile? = null,
    /**
     * Top[x] players will only get the drop.
     * If null, all players will get the drop.
     */
    val topPlayers: Int? = null,
    val rolls: Int = 1,
    val drops: MutableSet<Loot>
)


