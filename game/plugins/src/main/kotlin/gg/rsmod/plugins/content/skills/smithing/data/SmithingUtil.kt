package gg.rsmod.plugins.content.skills.smithing.data

/**
 * The map of Shayzien child ids to their names
 */
internal val shayzien: Map<Int, String> by lazy {
    val values = HashMap<Int, String>()

    for (i in 2 until 27 step 5) {
        val level = (i / 5) + 1
        values[i] = "shayzien supply gloves ($level)"
        values[i + 1] = "shayzien supply boots ($level)"
        values[i + 2] = "shayzien supply helm ($level)"
        values[i + 3] = "shayzien supply greaves ($level)"
        values[i + 4] = "shayzien supply platebody ($level)"
    }
    values
}

/**
 * Gets the bar id for a specified item name. I wasn't able to find any reference
 * to bars in the cache enums or item definitions, so I figured this would at least
 * achieve the desired affect.
 *
 * @param name  The name of the item
 * @return      The bar id
 */
internal fun getBar(name: String) : Bar? {
    return Bar.values.firstOrNull { name.startsWith(it.prefix) } ?: when {
        name.endsWith("lantern frame") -> Bar.IRON
        name.endsWith("lantern (unf)") -> Bar.STEEL
        name.endsWith("grapple tip") -> Bar.MITHRIL
        else -> null
    }
}

/**
 * Gets the item 'type' for a specified child id
 *
 * @param child The child id
 * @param bar   The bar type
 * @return      The type, as a string
 */
internal fun typeForChild(child: Int, bar: Bar): String? = when (child) {
    9 -> "${bar.prefix} dagger"
    10 -> "${bar.prefix} sword"
    11 -> "${bar.prefix} scimitar"
    12 -> "${bar.prefix} longsword"
    13 -> "${bar.prefix} 2h sword"
    14 -> "${bar.prefix} axe"
    15 -> "${bar.prefix} mace"
    16 -> "${bar.prefix} warhammer"
    17 -> "${bar.prefix} battleaxe"
    18 -> "${bar.prefix} claws"
    19 -> "${bar.prefix} chainbody"
    20 -> "${bar.prefix} platelegs"
    21 -> "${bar.prefix} plateskirt"
    22 -> "${bar.prefix} platebody"
    23 -> "${bar.prefix} nails"
    24 -> "${bar.prefix} med helm"
    25 -> "${bar.prefix} full helm"
    26 -> "${bar.prefix} sq shield"
    27 -> "${bar.prefix} kiteshield"
    28 -> when (bar) {
        Bar.IRON -> "lantern frame"
        Bar.STEEL -> "lantern (unf)"
        else -> null
    }
    29 -> "${bar.prefix} dart tip"
    30 -> "${bar.prefix} arrowtips"
    31 -> "${bar.prefix} knife"
    32 -> when (bar) {
        Bar.BRONZE -> "wire"
        Bar.IRON -> "spit"
        Bar.STEEL -> "studs"
        Bar.MITHRIL -> "grapple tip"
        else -> null
    }
    33 -> "${bar.prefix} bolts (unf)"
    34 -> "${bar.prefix} limbs"
    35 -> "${bar.prefix} javelin heads"
    else -> null
}