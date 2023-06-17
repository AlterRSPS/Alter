package gg.rsmod.plugins.content.items;

import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

/**
 *  @author <a href="https://github.com/CloudS3c">Cl0ud</a>
 *  @author <a href="https://www.rune-server.ee/members/376238-cloudsec/">Cl0ud</a>
 */


SpecialAttacks.register(Items.DRAGON_PICKAXE, 100, true) {
    player.getSkills().alterCurrentLevel(Skills.MINING, +3, 120)
    player.forceChat("Smashing!")
    player.animate(Animation.DRAGON_PICKAXE_SPECIAL)
    // @TODO Add special attacks sound.
}