package org.alter.plugins.content.combat.strategy.ranged.weapon

import org.alter.rscm.RSCM.getRSCM
import org.alter.plugins.content.combat.strategy.ranged.ammo.Bolts.ADAMANT_BOLTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Bolts.BLURITE_BOLTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Bolts.BOLT_RACKS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Bolts.BONE_BOLTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Bolts.BROAD_BOLTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Bolts.BRONZE_BOLTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Bolts.DRAGON_BOLTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Bolts.IRON_BOLTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Bolts.KEBBIT_BOLTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Bolts.RUNITE_BOLTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Bolts.STEEL_BOLTS

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class CrossbowType(val item: Int, val ammo: Array<Int>) {
    PHOENIX_CROSSBOW(item = getRSCM("item.phoenix_crossbow"), ammo = BRONZE_BOLTS),
    CROSSBOW(item = getRSCM("item.crossbow"), ammo = BRONZE_BOLTS),

    BRONZE_CROSSBOW(item = getRSCM("item.bronze_crossbow"), ammo = BRONZE_BOLTS),
    IRON_CROSSBOW(item = getRSCM("item.iron_crossbow"), ammo = BRONZE_BOLTS + IRON_BOLTS),
    STEEL_CROSSBOW(item = getRSCM("item.steel_crossbow"), ammo = BRONZE_BOLTS + IRON_BOLTS + STEEL_BOLTS),

    // @TODO MITH_CROSSBOW(item = Items.MITHRIL_CROSSBOW, ammo = BRONZE_BOLTS + IRON_BOLTS + STEEL_BOLTS + MITHRIL_BOLTS),
    ADAMANT_CROSSBOW(item = getRSCM("item.adamant_crossbow"), ammo = BRONZE_BOLTS + IRON_BOLTS + STEEL_BOLTS + ADAMANT_BOLTS),
    RUNE_CROSSBOW(item = getRSCM("item.rune_crossbow"), ammo = BRONZE_BOLTS + IRON_BOLTS + STEEL_BOLTS + ADAMANT_BOLTS + BROAD_BOLTS + RUNITE_BOLTS),
    DRAGON_CROSSBOW(
        item = getRSCM("item.dragon_crossbow"),
        ammo = BRONZE_BOLTS + IRON_BOLTS + STEEL_BOLTS + ADAMANT_BOLTS + BROAD_BOLTS + RUNITE_BOLTS + DRAGON_BOLTS,
    ),

    DRAGON_HUNTER_CROSSBOW(
        item = getRSCM("item.dragon_hunter_crossbow"),
        ammo = BRONZE_BOLTS + IRON_BOLTS + STEEL_BOLTS + ADAMANT_BOLTS + BROAD_BOLTS + RUNITE_BOLTS + DRAGON_BOLTS,
    ),
    ARMADYL_CROSSBOW(
        item = getRSCM("item.armadyl_crossbow"),
        ammo = BRONZE_BOLTS + IRON_BOLTS + STEEL_BOLTS + ADAMANT_BOLTS + BROAD_BOLTS + RUNITE_BOLTS + DRAGON_BOLTS,
    ),

    BLURITE_CROSSBOW(item = getRSCM("item.blurite_crossbow"), ammo = BRONZE_BOLTS + BLURITE_BOLTS),
    DORGESHUUN_CROSSBOW(item = getRSCM("item.dorgeshuun_crossbow"), ammo = BONE_BOLTS),
    HUNTER_CROSSBOW(item = getRSCM("item.hunters_crossbow"), ammo = KEBBIT_BOLTS),

    KARIL_CROSSBOW(item = getRSCM("item.karils_crossbow"), ammo = BOLT_RACKS),
    KARIL_CROSSBOW_0(item = getRSCM("item.karils_crossbow_0"), ammo = BOLT_RACKS),
    KARIL_CROSSBOW_25(item = getRSCM("item.karils_crossbow_25"), ammo = BOLT_RACKS),
    KARIL_CROSSBOW_50(item = getRSCM("item.karils_crossbow_50"), ammo = BOLT_RACKS),
    KARIL_CROSSBOW_75(item = getRSCM("item.karils_crossbow_75"), ammo = BOLT_RACKS),
    KARIL_CROSSBOW_100(item = getRSCM("item.karils_crossbow_100"), ammo = BOLT_RACKS),
    ;

    companion object {
        val values = enumValues<BowType>()
    }
}
