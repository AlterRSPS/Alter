package gg.rsmod.plugins.content.skills.prayer

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.message

/**
 * @author Fritz <frikkipafi@gmail.com>
 */
object Offer {

    fun canOffer(p: Player, bones: Bones): Boolean = true

    fun OfferBones(p: Player, bones: Bones, Altar: Int) {
        val boneName = p.world.definitions.get(ItemDef::class.java, bones.id).name
        val altars = arrayOf(bones.gilded, bones.ecto, bones.chaos)
        // Altar = 1(gilded), Altar = 2(ecto), Altar = 3(chaos)
        p.queue {
            p.addXp(Skills.PRAYER, altars[Altar + 1])
            p.animate(Prayer.ALTAR_ANIM)
            p.resetFacePawn()
            wait(3)
        }

        when(bones){
            else -> {
                p.message("You offer the ${boneName.toLowerCase()} to Gilded altar")
            }
        }
    }

}