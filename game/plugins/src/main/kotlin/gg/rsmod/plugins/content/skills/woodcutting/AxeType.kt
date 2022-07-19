package gg.rsmod.plugins.content.skills.woodcutting

import gg.rsmod.plugins.api.cfg.Animation
import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class AxeType(val item: Int, val level: Int, val animation: Int) {
    BRONZE(item = Items.BRONZE_AXE, level = 1, animation = Animation.BRONZE_CHOP),
    IRON(item = Items.IRON_AXE, level = 1, animation = Animation.IRON_CHOP),
    STEEL(item = Items.STEEL_AXE, level = 6, animation = Animation.STEEL_CHOP),
    BLACK(item = Items.BLACK_AXE, level = 11, animation = Animation.BLACK_CHOP),
    MITHRIL(item = Items.MITHRIL_AXE, level = 21, animation = Animation.MITHRIL_CHOP),
    ADAMANT(item = Items.ADAMANT_AXE, level = 31, animation = Animation.ADAMANT_CHOP),
    RUNE(item = Items.RUNE_AXE, level = 41, animation = Animation.RUNE_CHOP),
    DRAGON(item = Items.DRAGON_AXE, level = 61, animation = Animation.DRAGON_CHOP),
    INFERNAL(item = Items.INFERNAL_AXE, level = 61, animation = Animation.INFERNAL_CHOP),
    THIRDAGE(item = Items._3RD_AGE_AXE, level = 61, animation = Animation.THIRDAGE_CHOP),
    CRYSTAL(item = Items.CRYSTAL_AXE, level = 71, animation = Animation.CRYSTAL_CHOP);


    companion object {
        val values = enumValues<AxeType>()
    }
}