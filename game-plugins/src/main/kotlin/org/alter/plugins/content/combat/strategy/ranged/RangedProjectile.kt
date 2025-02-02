package org.alter.plugins.content.combat.strategy.ranged

import org.alter.api.ProjectileType
import org.alter.game.model.Graphic
import org.alter.rscm.RSCM.getRSCM
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.ADAMANT_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.AMETHYST_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.BRONZE_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.DRAGON_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.IRON_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.MITHRIL_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.OGRE_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.RUNE_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Arrows.STEEL_ARROWS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Bolts
import org.alter.plugins.content.combat.strategy.ranged.ammo.Darts.ADAMANT_DARTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Darts.BLACK_DARTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Darts.BRONZE_DARTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Darts.DRAGON_DARTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Darts.IRON_DARTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Darts.MITHRIL_DARTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Darts.RUNE_DARTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Darts.STEEL_DARTS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Javelins.ADAMANT_JAVELINS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Javelins.AMETHYST_JAVELINS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Javelins.BRONZE_JAVELINS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Javelins.DRAGON_JAVELINS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Javelins.IRON_JAVELINS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Javelins.MITHRIL_JAVELINS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Javelins.RUNE_JAVELINS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Javelins.STEEL_JAVELINS
import org.alter.plugins.content.combat.strategy.ranged.ammo.Knives.ADAMANT_KNIVES
import org.alter.plugins.content.combat.strategy.ranged.ammo.Knives.BLACK_KNIVES
import org.alter.plugins.content.combat.strategy.ranged.ammo.Knives.BRONZE_KNIVES
import org.alter.plugins.content.combat.strategy.ranged.ammo.Knives.DRAGON_KNIVES
import org.alter.plugins.content.combat.strategy.ranged.ammo.Knives.IRON_KNIVES
import org.alter.plugins.content.combat.strategy.ranged.ammo.Knives.MITHRIL_KNIVES
import org.alter.plugins.content.combat.strategy.ranged.ammo.Knives.RUNE_KNIVES
import org.alter.plugins.content.combat.strategy.ranged.ammo.Knives.STEEL_KNIVES

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class RangedProjectile(
    val gfx: Int,
    val drawback: Graphic? = null,
    val impact: Graphic? = null,
    val type: ProjectileType,
    val items: Array<Int>,
) {
    BOLTS(
        gfx = 27,
        type = ProjectileType.BOLT,
        items =
            Bolts.BRONZE_BOLTS + Bolts.IRON_BOLTS + Bolts.STEEL_BOLTS + Bolts.MITHRIL_BOLTS +
                Bolts.ADAMANT_BOLTS + Bolts.RUNITE_BOLTS + Bolts.DRAGON_BOLTS + Bolts.BLURITE_BOLTS + Bolts.KEBBIT_BOLTS + Bolts.BONE_BOLTS,
    ),

    BRONZE_ARROW(gfx = 10, drawback = Graphic(id = "graphics.bronze_arrow_drawback", height = 96), type = ProjectileType.ARROW, items = BRONZE_ARROWS),
    IRON_ARROW(gfx = 9, drawback = Graphic(id = "graphics.iron_arrow_drawback", height = 96), type = ProjectileType.ARROW, items = IRON_ARROWS),
    STEEL_ARROW(gfx = 11, drawback = Graphic(id = "graphics.steel_arrow_drawback", height = 96), type = ProjectileType.ARROW, items = STEEL_ARROWS),
    MITHRIL_ARROW(gfx = 12, drawback = Graphic(id = "graphics.mithril_arrow_drawback", height = 96), type = ProjectileType.ARROW, items = MITHRIL_ARROWS),
    ADAMANT_ARROW(gfx = 13, drawback = Graphic(id = "graphics.adamant_arrow_drawback", height = 96), type = ProjectileType.ARROW, items = ADAMANT_ARROWS),
    RUNE_ARROW(gfx = 15, drawback = Graphic(id = "graphics.rune_arrow_drawback", height = 96), type = ProjectileType.ARROW, items = RUNE_ARROWS),
    AMETHYST_ARROW(gfx = 1384, drawback = Graphic(id = "graphics.amethyst_arrow_drawback", height = 96), type = ProjectileType.ARROW, items = AMETHYST_ARROWS),
    DRAGON_ARROW(gfx = 1120, drawback = Graphic(id = "graphics.dual_dragon_arrow_drawback", height = 96), type = ProjectileType.ARROW, items = DRAGON_ARROWS),
    OGRE_ARROW(gfx = 242, drawback = Graphic(id = "graphics.ogre_arrow_drawback", height = 50), type = ProjectileType.ARROW, items = OGRE_ARROWS),

    BRONZE_JAVELIN(gfx = 200, type = ProjectileType.JAVELIN, items = BRONZE_JAVELINS),
    IRON_JAVELIN(gfx = 201, type = ProjectileType.JAVELIN, items = IRON_JAVELINS),
    STEEL_JAVELIN(gfx = 202, type = ProjectileType.JAVELIN, items = STEEL_JAVELINS),
    MITHRIL_JAVELIN(gfx = 203, type = ProjectileType.JAVELIN, items = MITHRIL_JAVELINS),
    ADAMANT_JAVELIN(gfx = 204, type = ProjectileType.JAVELIN, items = ADAMANT_JAVELINS),
    RUNE_JAVELIN(gfx = 205, type = ProjectileType.JAVELIN, items = RUNE_JAVELINS),
    AMETHYST_JAVELIN(gfx = 1386, type = ProjectileType.JAVELIN, items = AMETHYST_JAVELINS),
    DRAGON_JAVELIN(gfx = 1301, type = ProjectileType.JAVELIN, items = DRAGON_JAVELINS),

    BRONZE_KNIFE(gfx = 212, drawback = Graphic(id = "graphics.bronze_knife_drawback", height = 96), type = ProjectileType.THROWN, items = BRONZE_KNIVES),
    IRON_KNIFE(gfx = 213, drawback = Graphic(id = "graphics.iron_knife_drawback", height = 96), type = ProjectileType.THROWN, items = IRON_KNIVES),
    STEEL_KNIFE(gfx = 214, drawback = Graphic(id = "graphics.steel_knife_drawback", height = 96), type = ProjectileType.THROWN, items = STEEL_KNIVES),
    BLACK_KNIFE(gfx = 215, drawback = Graphic(id = "graphics.black_knife_drawback", height = 96), type = ProjectileType.THROWN, items = BLACK_KNIVES),
    MITHRIL_KNIFE(gfx = 216, drawback = Graphic(id = "graphics.mithril_knife_drawback", height = 96), type = ProjectileType.THROWN, items = MITHRIL_KNIVES),
    ADAMANT_KNIFE(gfx = 217, drawback = Graphic(id = "graphics.adamant_knife_drawback", height = 96), type = ProjectileType.THROWN, items = ADAMANT_KNIVES),
    RUNE_KNIFE(gfx = 218, drawback = Graphic(id = "graphics.rune_knife_drawback", height = 96), type = ProjectileType.THROWN, items = RUNE_KNIVES),
    DRAGON_KNIFE(gfx = 28, type = ProjectileType.THROWN, items = DRAGON_KNIVES),

    BRONZE_DART(gfx = 226, drawback = Graphic(id = "graphics.bronze_dart_drawback", height = 96), type = ProjectileType.THROWN, items = BRONZE_DARTS),
    IRON_DART(gfx = 227, drawback = Graphic(id = "graphics.iron_dart_drawback", height = 96), type = ProjectileType.THROWN, items = IRON_DARTS),
    STEEL_DART(gfx = 228, drawback = Graphic(id = "graphics.steel_dart_drawback", height = 96), type = ProjectileType.THROWN, items = STEEL_DARTS),
    BLACK_DART(gfx = 34, drawback = Graphic(id = "graphics.black_dart_drawback", height = 96), type = ProjectileType.THROWN, items = BLACK_DARTS),
    MITHRIL_DART(gfx = 229, drawback = Graphic(id = "graphics.mithril_dart_drawback", height = 96), type = ProjectileType.THROWN, items = MITHRIL_DARTS),
    ADAMANT_DART(gfx = 230, drawback = Graphic(id = "graphics.adamant_dart_drawback", height = 96), type = ProjectileType.THROWN, items = ADAMANT_DARTS),
    RUNE_DART(gfx = 231, drawback = Graphic(id = "graphics.rune_dart_drawback", height = 96), type = ProjectileType.THROWN, items = RUNE_DARTS),
    DRAGON_DART(gfx = 1122, drawback = Graphic(id = "graphics.dragon_dart_drawback", height = 96), type = ProjectileType.THROWN, items = DRAGON_DARTS),

    BRONZE_THROWING_AXE(
        gfx = 36,
        drawback = Graphic(id = "graphics.bronze_thrownaxe_drawback", height = 96),
        type = ProjectileType.THROWN,
        items = arrayOf(getRSCM("item.bronze_thrownaxe")),
    ),
    IRON_THROWING_AXE(
        gfx = 35,
        drawback = Graphic(id = "graphics.iron_thrownaxe_drawback", height = 96),
        type = ProjectileType.THROWN,
        items = arrayOf(getRSCM("item.iron_thrownaxe")),
    ),
    STEEL_THROWING_AXE(
        gfx = 37,
        drawback = Graphic(id = "graphics.steel_thrownaxe_drawback", height = 96),
        type = ProjectileType.THROWN,
        items = arrayOf(getRSCM("item.steel_thrownaxe")),
    ),
    MITHRIL_THROWING_AXE(
        gfx = 38,
        drawback = Graphic(id = "graphics.mithril_thrownaxe_drawback", height = 96),
        type = ProjectileType.THROWN,
        items = arrayOf(getRSCM("item.mithril_thrownaxe")),
    ),
    ADAMANT_THROWING_AXE(
        gfx = 39,
        drawback = Graphic(id = "graphics.adamant_thrownaxe_drawback", height = 96),
        type = ProjectileType.THROWN,
        items = arrayOf(getRSCM("item.adamant_thrownaxe")),
    ),
    RUNE_THROWING_AXE(
        gfx = 41,
        drawback = Graphic(id = "graphics.rune_thrownaxe_drawback", height = 96),
        type = ProjectileType.THROWN,
        items = arrayOf(getRSCM("item.rune_thrownaxe")),
    ),
    DRAGON_THROWING_AXE(
        gfx = 1319,
        drawback = Graphic(id = "graphics.dragon_thrownaxe_drawback", height = 96),
        type = ProjectileType.THROWN,
        items = arrayOf(getRSCM("item.dragon_thrownaxe")),
    ),

    TOKTZ_XIL_UL(gfx = 442, type = ProjectileType.THROWN, items = arrayOf(getRSCM("item.toktzxilul"))),

    GREY_CHINCHOMA(
        gfx = 908,
        impact = Graphic(id = "graphics.fire_wave_hit", height = 92),
        type = ProjectileType.THROWN,
        items = arrayOf(getRSCM("item.chinchompa_10033")),
    ),
    RED_CHINCHOMA(
        gfx = 909,
        impact = Graphic(id = "graphics.fire_wave_hit", height = 92),
        type = ProjectileType.THROWN,
        items = arrayOf(getRSCM("item.red_chinchompa_10034")),
    ),
    BLACK_CHINCHOMA(
        gfx = 1272,
        impact = Graphic(id = "graphics.fire_wave_hit", height = 92),
        type = ProjectileType.THROWN,
        items = arrayOf(getRSCM("item.black_chinchompa")),
    ),
    ;

    fun breakOnImpact(): Boolean =
        when (this) {
            GREY_CHINCHOMA, RED_CHINCHOMA, BLACK_CHINCHOMA -> true
            else -> false
        }

    companion object {
        val values = enumValues<RangedProjectile>()
    }
}
