import gg.rsmod.game.model.attr.COMBAT_TARGET_FOCUS_ATTR
import gg.rsmod.plugins.content.combat.dealHit




set_weapon_combat_logic(Items.TROLLWEISS) {
    val target = player.attr[COMBAT_TARGET_FOCUS_ATTR]?.get()
    val attackStyle = player.getAttackStyle()

    target?.let { it
        when (attackStyle) {
            0 -> {
                // Gfx: 986
                // Anim: 712
                player.animate(712)
                player.graphic(986)
                player.dealHit(target = it, maxHit = 1000, landHit = true, delay = 3)
            }

            1 -> {

            }

            2 -> {

            }

            3 -> {

            }

            else -> {
                player.message("AttackStyle: $attackStyle")
            }
        }
    }
}