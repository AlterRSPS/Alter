package gg.rsmod.plugins.content.skills.prayer

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Fritz <frikkipafi@gmail.com>
 */
enum class Bones(val id: Int, val xp: Double, val gilded: Double, val ecto: Double, val chaos: Double){
    NORMAL(Items.BONES, 4.5, 15.7, 18.0, 31.4),
    BURNT(Items.BURNT_BONES, 4.5, 15.7, 18.0, 31.4),
    WOLF(Items.WOLF_BONES, 4.5, 15.7, 18.0, 31.4),
    MONKEY(Items.MONKEY_BONES, 5.0, 17.5, 20.0, 35.0),
    BAT(Items.BAT_BONES, 5.3, 18.5, 21.2, 37.0),
    BIG(Items.BIG_BONES, 15.0, 52.5, 60.0, 105.0),
    JOGRE(Items.JOGRE_BONES, 15.0, 52.5, 60.0, 105.0),
    ZOGRE(Items.ZOGRE_BONES, 22.5, 78.7, 90.0, 157.5),
    SHAIKAHAN(Items.SHAIKAHAN_BONES, 25.0, 87.5, 100.0, 175.0),
    BABYDRAGON(Items.BABYDRAGON_BONES, 30.0, 105.0, 120.0, 210.0),
    WYRM(Items.WYRM_BONES, 50.0, 175.0, 200.0, 350.0),
    DRAGON(Items.DRAGON_BONES, 72.0, 252.0, 288.0, 504.0),
    WYVERN(Items.WYVERN_BONES, 72.0, 252.0, 288.0, 504.0),
    DRAKE(Items.DRAKE_BONES, 80.0, 280.0, 320.0, 560.0),
    FAYRG(Items.FAYRG_BONES, 84.0, 294.0, 336.0, 588.0),
    LAVA_DRAGON(Items.LAVA_DRAGON_BONES, 85.0, 297.5, 340.0, 595.0),
    RAURG(Items.RAURG_BONES, 96.0, 336.0, 384.0, 672.0),
    HYDRA(Items.HYDRA_BONES, 100.0, 385.0, 440.0, 770.0),
    DAGANNOTH(Items.DAGANNOTH_BONES, 125.0, 437.5, 500.0, 875.0),
    OURG(Items.OURG_BONES, 140.0, 490.0, 560.0, 980.0),
    SUPERIOR(Items.SUPERIOR_DRAGON_BONES, 150.0, 525.0, 600.0, 1050.0);

    companion object {
        val values = enumValues<Bones>()
    }

}