package gg.rsmod.plugins.content.skills.prayer

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.timer.BONE_OFFER_DELAY
import gg.rsmod.game.model.timer.BURY_BONE_DELAY
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.message

/**
 * @author Fritz <frikkipafi@gmail.com>
 */
object Offer {

    fun canOffer(p: Player, bones: Bones): Boolean = !p.timers.has(BONE_OFFER_DELAY)

    fun OfferBones(p: Player, bones: Bones, Altar: Int) {
        val delay = BURY_BONE_DELAY
        val boneName = p.world.definitions.get(ItemDef::class.java, bones.id).name
        val xp = bones.xp
        when (Altar) {
            //Gilded Altar
            1 -> {
                val xp = bones.gilded
            }
            //Ecto
            2 -> {
                val xp = bones.ecto
            }
            //Chaos Altar
            3 -> {
                val xp = bones.chaos
            }
        }
        p.addXp(Skills.PRAYER, xp)
        p.animate(Prayer.ALTAR_ANIM)
        p.resetFacePawn()
        p.timers[delay] = 3
        when(bones){
            else -> {
                p.message("You offer the ${boneName.toLowerCase()} to Gilded altar")
            }
        }
    }

}