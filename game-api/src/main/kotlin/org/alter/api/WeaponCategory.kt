package org.alter.api

enum class WeaponCategory(val id: List<Int>, val weaponName: String, val weaponType: Int) {
    BOW(listOf(64, 106), "Bow", 3),
    SLASH_SWORD(listOf(21), "Slash sword", 9),
    TWO_HANDED(listOf(61), "Two-handed sword", 23),
    AXE(listOf(35), "Axe", 1),
    BANNER(listOf(92, 42), "Banner", 25),
    BLUNT(listOf(26, 55, 15), "Blunt", 27),
    BULWARK(listOf(1014), "Bulwark", 28),
    CLAWS(listOf(65), "Claws", 4),
    PICKAXE(listOf(67), "Pickaxe", 11),
    POLEARM(listOf(66, 273), "Polearm", 12),
    SCYTHE(listOf(1143, 1193, 14), "Scythe", 14),
    SPEAR(listOf(36), "Spear", 15),
    SPIKED(listOf(39), "Spiked", 16),
    STAB_SWORD(listOf(25), "Stab sword", 17),
    UNARMED(listOf(188, -1, 148, 95, 1194, 0, 2053), "Unarmed", 0),
    WHIP(listOf(150), "Whip", 20),
    CHINCHOMPA(listOf(572), "Chinchompas", 7),
    CROSSBOW(listOf(567, 37), "Crossbow", 5),
    GUN(listOf(96), "Gun", 8),
    THROWN(listOf(24), "Thrown", 19),
    STAFF(listOf(1), "Staff", 18),
    SALAMANDER(listOf(586), "Salamander", 6),
    PARTISAN(listOf(1588), "Partisan", 17),
    MULTISTYLE(listOf(975), "Multi-style", 31),

    // Not able to equip
    FOOD(listOf(86), "Food", 0),
    SPRAY(listOf(886), "Fungicide", 0),
    EASTEREGG(listOf(319), "EasterEgg", 0),
    Potion(listOf(69), "Potion", 0)
    ;

    companion object {
        val values = values()

        fun get(id: Int): String {
            values.forEach {
                if (it.id.contains(id)) {
                    return it.weaponName
                }
            }
            return "Unknown type"
        }
    }
}
