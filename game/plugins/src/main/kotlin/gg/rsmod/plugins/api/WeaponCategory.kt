package gg.rsmod.plugins.api

enum class WeaponCategory(val id : List<Int>, val weaponName : String) {
    BOW(listOf(64, 106), "Bow"),
    SLASH_SWORD(listOf(21), "Slash sword"),
    TWO_HANDED(listOf(61), "Two-handed sword"),
    AXE(listOf(35), "Axe"),
    BANNER(listOf(92), "Banner"),
    BLUNT(listOf(26), "Blunt"),
    BULWARK(listOf(1014), "Bulwark"),
    CLAWS(listOf(65), "Claws"),
    PICKAXE(listOf(67), "Pickaxe"),
    POLEARM(listOf(66), "Polearm"),
    SCYTHE(listOf(1143), "Scythe"),
    SPEAR(listOf(36), "Spear"),
    SPIKED(listOf(39), "Spiked"),
    STAB_SWORD(listOf(25), "Stab sword"),
    UNARMED(listOf(188), "Unarmed"),
    WHIP(listOf(150), "Whip"),
    CHINCHOMPA(listOf(572), "Chinchompas"),
    CROSSBOW(listOf(567, 37), "Crossbow"),
    GUN(listOf(96), "Gun"),
    THROWN(listOf(24), "Thrown"),
    STAFF(listOf(1), "Staff"),
    SALAMANDER(listOf(586), "Salamander");

    companion object {
        val values = values()

        fun get(id: Int): String {
            values.forEach {
                if(it.id.contains(id)) {
                    return it.weaponName
                }
            }

            return "Unknown type"
        }
    }
}