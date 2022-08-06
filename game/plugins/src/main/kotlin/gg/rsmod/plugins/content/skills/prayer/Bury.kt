package gg.rsmod.plugins.content.skills.prayer

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.timer.BURY_BONE_DELAY
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.message

/**
 * @author Fritz <frikkipafi@gmail.com>
 */
object Bury {

    fun canBury(p: Player, bones: Bones): Boolean = !p.timers.has(BURY_BONE_DELAY)

    fun Bury(p: Player, bones: Bones) {
        val delay = BURY_BONE_DELAY
        val boneName = p.world.definitions.get(ItemDef::class.java, bones.id).name
        p.addXp(Skills.PRAYER, bones.xp)
        p.animate(Prayer.BURY_BONE_ANIM)
        p.resetFacePawn()
        p.timers[delay] = 3
        when(bones){
            else -> {
                p.message("You bury the ${boneName.toLowerCase()}")
            }
        }
    }
}