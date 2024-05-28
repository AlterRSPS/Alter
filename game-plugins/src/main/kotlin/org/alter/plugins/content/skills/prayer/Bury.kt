package org.alter.plugins.content.skills.prayer

import dev.openrune.cache.CacheManager.getItem
import org.alter.api.Skills
import org.alter.api.cfg.Animation
import org.alter.api.ext.message
import org.alter.game.model.entity.Player

/**
 * @author Fritz <frikkipafi@gmail.com>
 */
object Bury {
    fun canBury(p: Player, bones: Bones): Boolean = true

    fun Bury(p: Player, bones: Bones) {
            p.queue {
                p.lock()
                p.addXp(Skills.PRAYER, bones.xp)
                p.animate(Animation.BURY_BONE_ANIM)
                wait(3)
                p.unlock()
            }
        val boneName = getItem(bones.id).name
        when(bones){
            else -> {
                p.message("You bury a ${boneName.lowercase()}")
            }
        }
    }
}