package org.alter.game.service.game

import dev.openrune.cache.filestore.definition.data.ItemType

enum class WeaponCategory(val id: List<Int>, val weaponType: Int) {
    BOW(listOf(64, 106),3),
    SLASH_SWORD(listOf(21),9),
    TWO_HANDED(listOf(61),  23),
    AXE(listOf(35),  1),
    BANNER(listOf(92, 42), 25),
    BLUNT(listOf(26, 55, 15), 27),
    BULWARK(listOf(1014),28),
    CLAWS(listOf(65), 4),
    PICKAXE(listOf(67), 11),
    POLEARM(listOf(66, 273), 12),
    SCYTHE(listOf(1143, 1193, 14), 14),
    SPEAR(listOf(36),  15),
    SPIKED(listOf(39),  16),
    STAB_SWORD(listOf(25),  17),
    UNARMED(listOf(188, -1, 148, 95, 1194, 0, 2053),  0),
    WHIP(listOf(150),  20),
    CHINCHOMPA(listOf(572), 7),
    CROSSBOW(listOf(567, 37), 5),
    GUN(listOf(96), 8),
    THROWN(listOf(24),  19),
    STAFF(listOf(1), 18),
    SALAMANDER(listOf(586), 6),
    PARTISAN(listOf(1588), 17),
    MULTISTYLE(listOf(975), 31),

    // Not able to equip
    FOOD(listOf(86),  0),
    SPRAY(listOf(886),  0),
    EASTEREGG(listOf(319),  0),
    Potion(listOf(69),  0)
    ;

    companion object {
        val values = values()

        fun get(def: ItemType, id: Int): Int {
            values.forEach {
                if (it.id.contains(id)) {
                    return it.weaponType
                }
            }
            throw IllegalStateException("Unknown $id item ${def.id} at WeaponCategory")
        }
    }
}
