package org.alter.plugins.content.combat.strategy.ranged.ammo

import org.alter.rscm.RSCM.getRSCM

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Knives {
    val BRONZE_KNIVES = arrayOf(getRSCM("item.bronze_knife"), getRSCM("item.bronze_knifep"), getRSCM("item.bronze_knifep_5654"), getRSCM("item.bronze_knifep_5661"))
    val IRON_KNIVES = arrayOf(getRSCM("item.iron_knife"), getRSCM("item.iron_knifep"), getRSCM("item.iron_knifep_5655"), getRSCM("item.iron_knifep_5662"))
    val STEEL_KNIVES = arrayOf(getRSCM("item.steel_knife"), getRSCM("item.steel_knifep"), getRSCM("item.steel_knifep_5656"), getRSCM("item.steel_knifep_5663"))
    val BLACK_KNIVES = arrayOf(getRSCM("item.black_knife"), getRSCM("item.black_knifep"), getRSCM("item.black_knifep_5658"), getRSCM("item.black_knifep_5665"))
    val MITHRIL_KNIVES = arrayOf(getRSCM("item.mithril_knife"), getRSCM("item.mithril_knifep"), getRSCM("item.mithril_knifep_5657"), getRSCM("item.mithril_knifep_5664"))
    val ADAMANT_KNIVES = arrayOf(getRSCM("item.adamant_knife"), getRSCM("item.adamant_knifep"), getRSCM("item.adamant_knifep_5659"), getRSCM("item.adamant_knifep_5666"))
    val RUNE_KNIVES = arrayOf(getRSCM("item.rune_knife"), getRSCM("item.rune_knifep"), getRSCM("item.rune_knifep_5660"), getRSCM("item.rune_knifep_5667"))
    val DRAGON_KNIVES = arrayOf(getRSCM("item.dragon_knife"), getRSCM("item.dragon_knifep"), getRSCM("item.dragon_knifep_22808"), getRSCM("item.dragon_knifep_22810"))

    val KNIVES = BRONZE_KNIVES + IRON_KNIVES + STEEL_KNIVES + BLACK_KNIVES + MITHRIL_KNIVES + ADAMANT_KNIVES + RUNE_KNIVES + DRAGON_KNIVES
}
