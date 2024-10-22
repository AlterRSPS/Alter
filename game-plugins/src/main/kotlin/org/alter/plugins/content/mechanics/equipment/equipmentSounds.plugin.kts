import dev.openrune.cache.CacheManager

/**
 * @author CloudS3c 7/27/2024
 * As for now this is just randomly coded, just to give some sounds when you equip some shit.
 *
 * Notes:
 * On osrs unequip sounds are also played when you bank your full gear,
 *
 * When you bank full gear and have DarkBow in hand, it will play that.
 */
for (i in 0 until 13) {
    on_equip_to_slot(i) {
        val equipped = player.equipment[0]?.id
        if (equipped != null) {
            val item = CacheManager.getItem(equipped)
            with (item.name.lowercase()) {
                val sound = when {
                    contains("axe") -> Sound.EQUIP_AXE
                    contains("helm", "coif") -> Sound.EQUIP_HELMET
                    contains("whip") -> Sound.EQUIP_WHIP
                    contains("sword", "longsword", "dagger") -> Sound.EQUIP_SWORD
                    contains("sled") -> Sound.EQUIP_SLED
                    contains("body", "platebody") -> Sound.EQUIP_BODY
                    contains("legs") -> Sound.EQUIP_LEGS
                    contains("shield") -> Sound.EQUIP_SHIELD
                    contains("robe", "robeskirt", "leather") -> Sound.EQUIP_LEATHER
                    contains("sled", "magic", "wand", "staff") -> Sound.EQUIP_MAGIC
                    contains("darkbow") -> Sound.EQUIP_DARKBOW
                    else -> Sound.EQUIP_FUN
                }
                player.playSound(sound)
            }
        }
    }
}
fun String.contains(vararg keywords: String): Boolean {
    return keywords.any { keyword -> this.contains(keyword, ignoreCase = true) }
}