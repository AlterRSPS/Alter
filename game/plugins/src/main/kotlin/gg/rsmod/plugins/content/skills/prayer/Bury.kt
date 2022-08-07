package gg.rsmod.plugins.content.skills.prayer

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.message

/**
 * @author Fritz <frikkipafi@gmail.com>
 */
object Bury {
    fun canBury(p: Player, bones: Bones): Boolean = true

    fun Bury(p: Player, bones: Bones) {
            p.queue {
                p.addXp(Skills.PRAYER, bones.xp)
                p.animate(Prayer.BURY_BONE_ANIM)
                p.resetFacePawn()
                wait(3)
            }
        val boneName = p.world.definitions.get(ItemDef::class.java, bones.id).name
        when(bones){
            else -> {
                p.message("You bury the ${boneName.toLowerCase()}")
            }
        }
    }
}