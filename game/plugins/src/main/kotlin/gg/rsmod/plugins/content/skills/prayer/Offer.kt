package gg.rsmod.plugins.content.skills.prayer

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Animation
import gg.rsmod.plugins.api.ext.getInteractingItemSlot
import gg.rsmod.plugins.api.ext.message

/**
 * @author Fritz <frikkipafi@gmail.com>
 */
object Offer {

    fun canOffer(p: Player, bones: Bones): Boolean = true

    // Altar = 1(gilded), Altar = 2(ecto), Altar = 3(chaos)
    fun OfferBones(p: Player, bones: Bones, Altar: Int) {
        val boneName = p.world.definitions.get(ItemDef::class.java, bones.id).name
        val inventorySlot = p.getInteractingItemSlot()
        p.queue {
            p.lock()
            when (Altar) {
                1 -> {  p.addXp(Skills.PRAYER, bones.gilded)
                        p.message("You offer ${boneName} to the gilded altar.")}
                2 -> {  p.addXp(Skills.PRAYER, bones.ecto)
                        p.message("You offer ${boneName} to the ectoplasmic altar.") }
                3 -> {  if (p.world.chance(1, 2)) { p.message("You saved a ${boneName}.") }
                        else { p.inventory.remove(item = bones.id, beginSlot = inventorySlot).hasSucceeded()
                                p.message("You offer ${boneName} to the chaos altar.")
                             }
                        p.addXp(Skills.PRAYER, bones.chaos)
                     }
            }
            p.animate(Animation.OFFER_BONES_TO_ALTER_ANIM)
            p.resetFacePawn()
            wait(3)
            p.unlock()
        }
    }

}