package gg.rsmod.plugins.content.skills.mining

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Fritz <frikkipafi@gmail.com>
 */

enum class InvRockType(val itemId : Int, val levelReq : Int, val exp : Double, val difficulty : Int, val respawn : Int) {
    //normal ores
    CLAY(Items.CLAY, 1, 5.0, 1, 4),
    COPPER(Items.COPPER_ORE, 1, 17.5, 1, 4),
    TIN(Items.TIN_ORE, 1, 17.5, 1, 4),
    IRON(Items.IRON_ORE, 15, 35.0, 2, 7),
    SILVER(Items.SILVER_ORE, 20, 40.0, 2, 4),
    COAL(Items.COAL, 30, 50.0, 3, 7),
    GOLD(Items.GOLD_ORE, 40, 65.0, 4, 10),
    MITHRIL(Items.MITHRIL_ORE, 55, 80.0, 6, 12),
    ADAMANTITE(Items.ADAMANTITE_ORE, 70, 95.0, 8, 15),
    RUNITE(Items.RUNITE_ORE, 85, 125.0, 10, 22),
    AMETHYST(Items.AMETHYST, 92, 240.0, 4, 6),

    //Essences
    RUNE_ESSENCE(Items.RUNE_ESSENCE, 1, 5.0, 1, 0),
    PURE_ESSENCE(Items.PURE_ESSENCE, 30, 5.0, 1, 0),
    DENSE_ESSENCE_BLOCK(Items.DENSE_ESSENCE_BLOCK, 38, 12.0, 1, 0),

    //Other ores
    LIMESTONE(Items.LIMESTONE, 10, 26.5, 1, 4),
    BLURITE(Items.BLURITE_ORE, 10, 17.5, 1, 4),
    ELEMENTAL(Items.ELEMENTAL_ORE, 20, 0.0, 2, 4),
    DAEYALT(Items.DAEYALT_ORE, 20, 17.5, 2, 4),
    VOLCANIC_ASH(Items.VOLCANIC_ASH, 22, 10.0, 2, 6),
    PAY_DIRT(Items.PAYDIRT, 30, 60.0, 3, 7),

    VOLCANIC_SULPHUR(Items.VOLCANIC_SULPHUR, 42, 25.0, 4, 10),
    LUNAR(Items.LUNAR_ORE, 60, 0.0, 7, 13),
    LOVAKITE(Items.LOVAKITE_ORE, 65, 10.0, 7, 14),


    // want val SAND instead of Items.SANDSTONE_1KG
    SANDSTONE(Items.SANDSTONE_1KG, 35, 30.0-60.0, 1, 4),
    // want val GRAN instead of Items.GRANITE_500G
    GRANITE(Items.GRANITE_500G, 40, 65.0, 4, 10);

    companion object {
        val values = enumValues<InvRockType>()

        val SAND = intArrayOf(
            Items.SANDSTONE_1KG,
            Items.SANDSTONE_2KG,
            Items.SANDSTONE_5KG,
            Items.SANDSTONE_10KG,
            Items.SANDSTONE_20KG,
            Items.SANDSTONE_32KG
        )
        val GRAN = intArrayOf(
            Items.GRANITE_500G,
            Items.GRANITE_2KG,
            Items.GRANITE_5KG
        )
    }
}

