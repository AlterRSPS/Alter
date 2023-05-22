package gg.rsmod.plugins.content.combat.weapons

import gg.rsmod.plugins.content.combat.createProjectile
import gg.rsmod.plugins.content.combat.dealHit


/**
 * @author CloudS3c 3/22/2023
 */
set_item_combat_logic(Items.TRIDENT_OF_THE_SEAS) {
    val target = player.getTarget()
    target?.let {
        player.animate(Animation.HUMAN_POWERED_STAFF_ATTACK)
        player.graphic(Graphics.CAST_SEAS_TRIDENT, 92, 0)
        player.createProjectile(it, 1253, ProjectileType.MAGIC, 15)
        player.dealHit(target = it, maxHit = 90, landHit = true, delay = 1)
        player.playSound(Sound.CONTACT_DARKNESS_IMPACT)
    }
}

set_item_combat_logic(Items.DRAGONFIRE_SHIELD) {
    val target = player.getTarget()
    target?.let {
        player.message("Your shield burns the enemy.")
    }
}

set_item_combat_logic(Items.BERSERKER_RING) {
    val target = player.getTarget()
    target?.let {
        if (target is Npc) {
            player.message("Your berserker ring tints ${target.name}")
            target.applyTint(hue = 0, saturation = 6, luminance = 28, opacity = 112, delay = 0, duration = 240)
        }
    }
}
