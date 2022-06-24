package gg.rsmod.plugins.content.items.adminitems

import gg.rsmod.game.model.attr.COMBAT_TARGET_FOCUS_ATTR
import gg.rsmod.plugins.content.combat.dealHit

set_weapon_combat_logic(Items.ARMADYL_GODSWORD_22665) {
    val target = pawn.attr[COMBAT_TARGET_FOCUS_ATTR]!!.get()
    target?.let {
        target.graphic(111)
        player.animate(1058)
        pawn.dealHit(target = target, maxHit = 9999, landHit = true, delay = 0)
    }
}

