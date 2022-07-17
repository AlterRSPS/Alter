package gg.rsmod.plugins.content.skills.mining

import gg.rsmod.plugins.api.cfg.Items


/**
 * @author Fritz <frikkipafi@gmail.com>
 */
enum class PickAxeType(val item: Int, val level: Int, val animation: Int) {
    BRONZE(Items.BRONZE_PICKAXE, level = 1, animation = 625),
    IRON( Items.IRON_PICKAXE, level = 1, animation = 626),
    STEEL(Items.STEEL_PICKAXE, level = 6, animation = 627),
    BLACK(Items.BLACK_PICKAXE, level = 11, animation = 3873),
    MITHRIL(Items.MITHRIL_PICKAXE, level = 21, animation = 629),
    ADAMANT(Items.ADAMANT_PICKAXE, level = 31, animation = 628),
    RUNE(Items.RUNE_PICKAXE, level = 41, animation = 624),
    DRAGON(Items.DRAGON_PICKAXE, level = 61, animation = 7139),
    INFERNAL(Items.INFERNAL_PICKAXE, level = 61, animation = 4482),
    THIRDAGE(Items._3RD_AGE_PICKAXE, level = 61, animation = 7283),
    CRYSTAL(Items.CRYSTAL_PICKAXE, level = 71, animation = 8347);

    companion object {
        val values = enumValues<PickAxeType>()
    }
}