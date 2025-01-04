package org.alter.plugins.content.combat.strategy.ranged.weapon

import org.alter.rscm.RSCM.getRSCM
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.ADAMANT_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.AMETHYST_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.BRONZE_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.BRUTAL_ADAMANT_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.BRUTAL_BLACK_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.BRUTAL_BRONZE_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.BRUTAL_IRON_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.BRUTAL_MITHRIL_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.BRUTAL_RUNE_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.BRUTAL_STEEL_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.DRAGON_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.IRON_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.MITHRIL_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.OGRE_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.RUNE_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.STEEL_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.TRAINING_ARROWS

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class BowType(val item: Int, val ammo: Array<Int>) {
    TRAINING_BOW(item = getRSCM("item.training_bow"), ammo = TRAINING_ARROWS),

    SHORTBOW(item = getRSCM("item.shortbow"), ammo = BRONZE_ARROWS + IRON_ARROWS),
    LONGBOW(item = getRSCM("item.longbow"), ammo = BRONZE_ARROWS + IRON_ARROWS),

    OAK_SHORTBOW(item = getRSCM("item.oak_shortbow"), ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS),
    OAK_LONGBOW(item = getRSCM("item.oak_longbow"), ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS),

    WILLOW_SHORTBOW(item = getRSCM("item.willow_shortbow"), ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS),
    WILLOW_COMP_BOW(item = getRSCM("item.willow_comp_bow"), ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS),
    WILLOW_LONGBOW(item = getRSCM("item.willow_longbow"), ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS),

    MAPLE_SHORTBOW(item = getRSCM("item.maple_shortbow"), ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS),
    MAPLE_LONGBOW(item = getRSCM("item.maple_longbow"), ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS),

    OGRE_BOW(item = getRSCM("item.ogre_bow"), ammo = OGRE_ARROWS),
    COMP_OGRE_BOW(
        item = getRSCM("item.comp_ogre_bow"),
        ammo = BRUTAL_BRONZE_ARROWS + BRUTAL_IRON_ARROWS + BRUTAL_STEEL_ARROWS + BRUTAL_BLACK_ARROWS + BRUTAL_MITHRIL_ARROWS + BRUTAL_ADAMANT_ARROWS + BRUTAL_RUNE_ARROWS,
    ),

    YEW_SHORTBOW(
        item = getRSCM("item.yew_shortbow"),
        ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS,
    ),
    YEW_LONGBOW(
        item = getRSCM("item.yew_longbow"),
        ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS,
    ),
    YEW_COMP_BOW(
        item = getRSCM("item.yew_comp_bow"),
        ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS,
    ),

    MAGIC_SHORTBOW(
        item = getRSCM("item.magic_shortbow"),
        ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS,
    ),
    MAGIC_SHORTBOW_I(
        item = getRSCM("item.magic_shortbow_i"),
        ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS,
    ),
    MAGIC_LONGBOW(
        item = getRSCM("item.magic_longbow"),
        ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS,
    ),
    MAGIC_COMP_BOW(
        item = getRSCM("item.magic_comp_bow"),
        ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS,
    ),

    SEERCULL(
        item = getRSCM("item.seercull"),
        ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS,
    ),
    CRAWS_BOW(item = getRSCM("item.craws_bow"), ammo = emptyArray()),

    DARK_BOW(
        item = getRSCM("item.dark_bow"),
        ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS + DRAGON_ARROWS,
    ),
    BLUE_DARK_BOW(
        item = getRSCM("item.dark_bow_12765"),
        ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS + DRAGON_ARROWS,
    ),
    GREEN_DARK_BOW(
        item = getRSCM("item.dark_bow_12766"),
        ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS + DRAGON_ARROWS,
    ),
    WHITE_DARK_BOW(
        item = getRSCM("item.dark_bow_12767"),
        ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS + DRAGON_ARROWS,
    ),
    YELLOW_DARK_BOW(
        item = getRSCM("item.dark_bow_12768"),
        ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS + DRAGON_ARROWS,
    ),

    THIRD_AGE_BOW(
        item = getRSCM("item._3rd_age_bow"),
        ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS + DRAGON_ARROWS,
    ),

    CRYSTAL_BOW_110(item = getRSCM("item.crystal_bow_110"), ammo = emptyArray()),
    CRYSTAL_BOW_210(item = getRSCM("item.crystal_bow_210"), ammo = emptyArray()),
    CRYSTAL_BOW_310(item = getRSCM("item.crystal_bow_310"), ammo = emptyArray()),
    CRYSTAL_BOW_410(item = getRSCM("item.crystal_bow_410"), ammo = emptyArray()),
    CRYSTAL_BOW_510(item = getRSCM("item.crystal_bow_510"), ammo = emptyArray()),
    CRYSTAL_BOW_610(item = getRSCM("item.crystal_bow_610"), ammo = emptyArray()),
    CRYSTAL_BOW_710(item = getRSCM("item.crystal_bow_710"), ammo = emptyArray()),
    CRYSTAL_BOW_810(item = getRSCM("item.crystal_bow_810"), ammo = emptyArray()),
    CRYSTAL_BOW_910(item = getRSCM("item.crystal_bow_910"), ammo = emptyArray()),
    CRYSTAL_BOW_FULL(item = getRSCM("item.crystal_bow_full"), ammo = emptyArray()),
    CRYSTAL_BOW_NEW(item = getRSCM("item.new_crystal_bow"), ammo = emptyArray()),

    TWISTED_BOW(
        item = getRSCM("item.twisted_bow"),
        ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS + DRAGON_ARROWS,
    ),
    ;

    companion object {
        val values = enumValues<BowType>()
    }
}
