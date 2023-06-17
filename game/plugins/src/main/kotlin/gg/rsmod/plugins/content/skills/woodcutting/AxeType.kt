package gg.rsmod.plugins.content.skills.woodcutting

import gg.rsmod.plugins.api.cfg.Animation
import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class AxeType(val item: Int, val level: Int, val animation: Int) {
    BRONZE(item = Items.BRONZE_AXE, level = 1, animation = Animation.WOODCUTTING_BRONZE_AXE),
    IRON(item = Items.IRON_AXE, level = 1, animation = Animation.WOODCUTTING_IRON_AXE),
    STEEL(item = Items.STEEL_AXE, level = 6, animation = Animation.WOODCUTTING_STEEL_AXE),
    BLACK(item = Items.BLACK_AXE, level = 11, animation = Animation.WOODCUTTING_BLACK_AXE),
    MITHRIL(item = Items.MITHRIL_AXE, level = 21, animation = Animation.WOODCUTTING_MITHRIL_AXE),
    ADAMANT(item = Items.ADAMANT_AXE, level = 31, animation = Animation.WOODCUTTING_ADAMANT_AXE),
    RUNE(item = Items.RUNE_AXE, level = 41, animation = Animation.WOODCUTTING_RUNE_AXE),
    DRAGON(item = Items.DRAGON_AXE, level = 61, animation = Animation.WOODCUTTING_DRAGON_AXE),
    INFERNAL(item = Items.INFERNAL_AXE, level = 61, animation = Animation.WOODCUTTING_INFERNAL_AXE),
    THIRDAGE(item = Items._3RD_AGE_AXE, level = 61, animation = Animation.WOODCUTTING_THIRDAGE_AXE),
    CRYSTAL(item = Items.CRYSTAL_AXE, level = 71, animation = Animation.WOODCUTTING_CRYSTAL_AXE);


    companion object {
        val values = enumValues<AxeType>()
    }
}