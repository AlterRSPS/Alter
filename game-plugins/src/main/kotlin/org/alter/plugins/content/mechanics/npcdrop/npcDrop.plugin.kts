import org.alter.game.model.weightedTableBuilder.Loot
import org.alter.game.model.weightedTableBuilder.LootTable
import org.alter.game.model.weightedTableBuilder.TableType
import kotlin.reflect.KFunction
import kotlin.reflect.typeOf

/**
 * @author CloudS3c
 */
on_any_npc_death {
    npc.damageMap.getMostDamage()?.let {    pawn ->
        val player = pawn as Player
        // Preprocess the loot tables once
        val lootTables = npc.combatDef.LootTables ?: return@let
        val tables = TableType.values().associateWith { tableType ->
            lootTables.filter { it.tableType == tableType }
        }
        val position: Tile = tables[TableType.ALWAYS]?.firstOrNull()?.dropPosition ?: npc.getCentreTile()

        // Process ALWAYS loot tables immediately
        tables[TableType.ALWAYS]?.forEach { processLootTable(npc, player, it) }

        val loot: MutableList<Loot?> = mutableListOf()

        // Add TERTIARY loot if present
        tables[TableType.TERTIARY]?.firstOrNull()?.tertiaryRoll()?.let { loot.addAll(it) }

        // Process PRE_ROLL and MAIN loot tables based on drop rolls
        repeat(tables[TableType.MAIN]?.firstOrNull()?.rolls ?: 1) {
            tables[TableType.PRE_ROLL]?.firstOrNull()?.preRoll()?.let {
                loot.add(it)
            } ?: tables[TableType.MAIN]?.firstOrNull()?.mainRoll()?.let {
                loot.add(it)
            }
        }

        // Distribute loot if any
        loot.filterNotNull().forEach {
            distributeLootIfEligible(npc, player, position = position, loot = it)
        }
    }
}



/**
 * @TODO
 * Export to [LootHandler] so that we could reuse it on other things aswell.
 * Return only loot
 */

private fun processLootTable(npc: Npc, player: Player, lootTable: LootTable) {
    val position = lootTable.dropPosition ?: npc.getCentreTile()
    lootTable.drops.forEach { drop ->
        distributeLootIfEligible(npc, player, drop, position)
    }
}

private fun distributeLootIfEligible(npc: Npc, player: Player, loot: Loot, position: Tile) {
    if (!loot.block.invoke(player)) return
    when (val item = loot.item) {
        is Int -> giveLoot(npc, player, loot, position)
        is KFunction<*> -> processDynamicLoot(npc, player, item, position)
        is LootTable -> processLootTable(npc, player, item)
        is Loot -> giveLoot(npc, player, item, position)
        else -> throw IllegalStateException("Unhandled Drop type: ${loot::class.java}")
    }
}
private fun processDynamicLoot(npc: Npc, player: Player, itemFunction: KFunction<*>, position: Tile) {
    try {
        if (itemFunction.returnType == typeOf<LootTable>()) {
            val result = itemFunction.call() as LootTable
            result.drops.forEach {
                giveLoot(npc, player, it, position)
            }
        } else if (itemFunction.returnType == typeOf<Loot>()) {
            val result = itemFunction.call() as Loot
            giveLoot(npc, player, result, position)
        } else {
            throw IllegalStateException("Unhandled return type: ${itemFunction.returnType}")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun giveLoot(npc: Npc, player: Player, loot: Loot, position: Tile) {
    npc.world.spawn(GroundItem(Item(id = loot.item as Int, amount = randomStep(loot.min, loot.max, loot.steepness)), position, player))
}
