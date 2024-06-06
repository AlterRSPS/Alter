package org.alter.plugins.content.mechanics.poison

import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player

/**
 * @author Tom <rspsmods@gmail.com>
 */

fun Pawn.poison(
    initialDamage: Int,
    onPoison: () -> Unit,
) {
    if (!Poison.isImmune(this) && Poison.poison(this, initialDamage)) {
        if (this is Player) {
            Poison.setHpOrb(this, Poison.OrbState.POISON)
        }
        onPoison()
    }
}
