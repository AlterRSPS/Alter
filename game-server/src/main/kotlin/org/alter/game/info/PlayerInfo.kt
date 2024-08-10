package org.alter.game.info

import dev.openrune.cache.CacheManager
import org.alter.game.model.AnimationSet
import org.alter.game.model.appearance.Gender
import org.alter.game.model.entity.Player

class PlayerInfo(var player: Player) {

    val DEFAULT_ANIM_SET = AnimationSet(
        readyAnim = 808,
        turnAnim = 823,
        walkAnim = 819,
        walkAnimBack = 820,
        walkAnimLeft = 821,
        walkAnimRight = 822,
        runAnim = 824
    )

    fun syncAppearance() {
        val info = player.avatar.extendedInfo
        if (player.getTransmogId() == -1) {
            for (i in 0 until 7) {
                info.setIdentKit(i, player.appearance.getLook(i))
            }
            for (i in 0 until 12) {
                val obj = player.equipment[i]
                if (obj == null) {
                    info.setWornObj(i, -1, -1, -1)
                } else {
                    val config = CacheManager.getItem(obj.id)
                    var wearPos2 = -1
                    var wearPos3 = -1
                    if (config.appearanceOverride1 != 0) {
                        wearPos2 = config.appearanceOverride1
                    }
                    if (config.appearanceOverride2 != 0) {
                        wearPos3 = config.appearanceOverride2
                    }
                    info.setWornObj(i, obj.id, wearPos2, wearPos3)
                }
            }
            var animSequance = DEFAULT_ANIM_SET
            val weapon = player.equipment[3]
            if (weapon != null) {
                val renderAnimationSet = CacheManager.getItem(weapon.id).renderAnimations
                if (renderAnimationSet != null) {
                    animSequance = AnimationSet(
                        readyAnim = renderAnimationSet[0],
                        turnAnim = renderAnimationSet[1],
                        walkAnim = renderAnimationSet[2],
                        walkAnimBack = renderAnimationSet[3],
                        walkAnimLeft = renderAnimationSet[4],
                        walkAnimRight = renderAnimationSet[5],
                        runAnim = renderAnimationSet[6]
                    )
                }
            }
            player.avatar.extendedInfo.setBaseAnimationSet(
                readyAnim = animSequance.readyAnim,
                turnAnim = animSequance.turnAnim,
                walkAnim = animSequance.walkAnim,
                walkAnimBack = animSequance.walkAnimBack,
                walkAnimLeft = animSequance.walkAnimLeft,
                walkAnimRight = animSequance.walkAnimRight,
                runAnim = animSequance.runAnim,
            )
            for (i in 0 until 5) {
                info.setColour(i, player.appearance.colors[i])
            }
        } else {
            val def = CacheManager.getNpc(player.getTransmogId())
            val animSequance = AnimationSet(
                readyAnim = def.standAnim,
                walkAnim = def.walkAnim,
                walkAnimBack = def.walkAnim,
                walkAnimLeft = def.rotateLeftAnim,
                walkAnimRight = def.rotateRightAnim,
                turnAnim = def.rotateBackAnim,
                runAnim = def.walkAnim
            )
            player.avatar.extendedInfo.setBaseAnimationSet(
                readyAnim = animSequance.readyAnim,
                turnAnim = animSequance.turnAnim,
                walkAnim = animSequance.walkAnim,
                walkAnimBack = animSequance.walkAnimBack,
                walkAnimLeft = animSequance.walkAnimLeft,
                walkAnimRight = animSequance.walkAnimRight,
                runAnim = animSequance.runAnim,
            )
        }
        info.setName(player.username)
        info.setCombatLevel(player.combatLevel)
        info.setMale(player.appearance.gender == Gender.MALE)
        info.setOverheadIcon(player.prayerIcon)
        info.setSkullIcon(player.skullIcon)
        info.transformToNpc(player.getTransmogId())
        info.setHidden(player.invisible)
    }



}