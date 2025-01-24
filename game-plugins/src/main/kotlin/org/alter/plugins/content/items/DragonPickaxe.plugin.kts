package org.alter.plugins.content.items

import org.alter.plugins.content.combat.specialattack.SpecialAttacks

/**
 *  @author <a href="https://github.com/CloudS3c">Cl0ud</a>
 *  @author <a href="https://www.rune-server.ee/members/376238-cloudsec/">Cl0ud</a>
 */

SpecialAttacks.register("item.dragon_pickaxe", 100, true) {
    player.getSkills().alterCurrentLevel(Skills.MINING, +3, 120)
    player.forceChat("Smashing!")
    player.animate(Animation.DRAGON_PICKAXE_SPECIAL)
}
