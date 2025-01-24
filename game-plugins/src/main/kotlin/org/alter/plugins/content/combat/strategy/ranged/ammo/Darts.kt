package org.alter.plugins.content.combat.strategy.ranged.ammo

import org.alter.rscm.RSCM.getRSCM

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Darts {
    val BRONZE_DARTS = arrayOf(getRSCM("item.bronze_dart"), getRSCM("item.bronze_dartp"), getRSCM("item.bronze_dartp_5628"), getRSCM("item.bronze_dartp_5635"))
    val IRON_DARTS = arrayOf(getRSCM("item.iron_dart"), getRSCM("item.iron_dartp")/*, Items.IRON_DARTP_5629 */, getRSCM("item.iron_dartp_5636"))
    val STEEL_DARTS = arrayOf(getRSCM("item.steel_dart"), getRSCM("item.steel_dartp"), getRSCM("item.steel_dartp_5630"), getRSCM("item.steel_dartp_5637"))
    val BLACK_DARTS = arrayOf(getRSCM("item.black_dart"), getRSCM("item.black_dartp"), getRSCM("item.black_dartp_5631"), getRSCM("item.black_dartp_5638"))
    val MITHRIL_DARTS = arrayOf(getRSCM("item.mithril_dart"), getRSCM("item.mithril_dartp"), getRSCM("item.mithril_dartp_5632"), getRSCM("item.mithril_dartp_5639"))
    val ADAMANT_DARTS = arrayOf(getRSCM("item.adamant_dart"), getRSCM("item.adamant_dartp"), getRSCM("item.adamant_dartp_5633"), getRSCM("item.adamant_dartp_5640"))
    val RUNE_DARTS = arrayOf(getRSCM("item.rune_dart"), getRSCM("item.rune_dartp"), getRSCM("item.rune_dartp_5634"), getRSCM("item.rune_dartp_5641"))
    val DRAGON_DARTS = arrayOf(getRSCM("item.dragon_dart"), getRSCM("item.dragon_dartp"), getRSCM("item.dragon_dartp_11233"), getRSCM("item.dragon_dartp_11234"))

    val DARTS = BRONZE_DARTS + IRON_DARTS + STEEL_DARTS + BLACK_DARTS + MITHRIL_DARTS + ADAMANT_DARTS + RUNE_DARTS + DRAGON_DARTS
}
