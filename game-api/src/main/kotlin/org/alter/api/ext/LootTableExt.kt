package org.alter.api.ext

import org.alter.api.dsl.WeightedTableBuilder
import org.alter.game.model.weightedTableBuilder.Loot
import org.alter.game.model.weightedTableBuilder.LootTable
import java.security.SecureRandom
import kotlin.random.Random

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
            if (Random.nextInt(loot.weight!!) == 0) {
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
            if (Random.nextInt(loot.weight!!) == 0) {
                items += loot
            }
        }
    }
    return items
}

fun dropTable(init: WeightedTableBuilder.() -> Unit): WeightedTableBuilder {
    val builder = WeightedTableBuilder()
    builder.init()
    return builder
}

fun normalRoll() {

}