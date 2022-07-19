package gg.rsmod.plugins.content.skills.mining

import gg.rsmod.plugins.api.cfg.Animation
import gg.rsmod.plugins.api.cfg.Items


/**
 * @author Fritz <frikkipafi@gmail.com>
 */
enum class PickAxeType(val item: Int, val level: Int, val animation: Int) {
    BRONZE(Items.BRONZE_PICKAXE, level = 1, Animation.BRONZE_MINE),
    IRON(Items.IRON_PICKAXE, level = 1, Animation.IRON_MINE),
    STEEL(Items.STEEL_PICKAXE, level = 6, Animation.STEEL_MINE),
    BLACK(Items.BLACK_PICKAXE, level = 11, Animation.BLACK_MINE),
    MITHRIL(Items.MITHRIL_PICKAXE, level = 21, Animation.MITHRIL_MINE),
    ADAMANT(Items.ADAMANT_PICKAXE, level = 31, Animation.ADAMANT_MINE),
    RUNE(Items.RUNE_PICKAXE, level = 41, Animation.RUNE_MINE),
    DRAGON(Items.DRAGON_PICKAXE, level = 61, Animation.DRAGON_MINE),
    INFERNAL(Items.INFERNAL_PICKAXE, level = 61, Animation.INFERNAL_MINE),
    THIRDAGE(Items._3RD_AGE_PICKAXE, level = 61, Animation.THIRDAGE_MINE),
    CRYSTAL(Items.CRYSTAL_PICKAXE, level = 71, Animation.CRYSTAL_MINE);

    companion object {
        val values = enumValues<PickAxeType>()
    }
}