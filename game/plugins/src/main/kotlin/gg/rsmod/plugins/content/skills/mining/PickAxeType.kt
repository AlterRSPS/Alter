package gg.rsmod.plugins.content.skills.mining

import gg.rsmod.plugins.api.cfg.Animation
import gg.rsmod.plugins.api.cfg.Items


/**
 * @author Fritz <frikkipafi@gmail.com>
 */
enum class PickAxeType(val item: Int, val level: Int, val animation: Int) {
    BRONZE(Items.BRONZE_PICKAXE, level = 1, Animation.MINING_BRONZE_PICKAXE),
    IRON(Items.IRON_PICKAXE, level = 1, Animation.MINING_IRON_PICKAXE),
    STEEL(Items.STEEL_PICKAXE, level = 6, Animation.MINING_STEEL_PICKAXE),
    BLACK(Items.BLACK_PICKAXE, level = 11, Animation.MINING_BLACK_PICKAXE),
    MITHRIL(Items.MITHRIL_PICKAXE, level = 21, Animation.MINING_MITHRIL_PICKAXE),
    ADAMANT(Items.ADAMANT_PICKAXE, level = 31, Animation.MINING_ADAMANT_PICKAXE),
    RUNE(Items.RUNE_PICKAXE, level = 41, Animation.MINING_RUNE_PICKAXE),
    DRAGON(Items.DRAGON_PICKAXE, level = 61, Animation.MINING_DRAGON_PICKAXE),
    INFERNAL(Items.INFERNAL_PICKAXE, level = 61, Animation.MINING_INFERNAL_PICKAXE),
    THIRDAGE(Items._3RD_AGE_PICKAXE, level = 61, Animation.MINING_THIRDAGE_PICKAXE),
    CRYSTAL(Items.CRYSTAL_PICKAXE, level = 71, Animation.MINING_CRYSTAL_PICKAXE);

    companion object {
        val values = enumValues<PickAxeType>()
    }
}