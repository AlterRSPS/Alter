package org.alter.plugins.content.combat.formula

import org.alter.game.model.entity.Pawn

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface CombatFormula {
    fun getAccuracy(
        pawn: Pawn,
        target: Pawn,
        specialAttackMultiplier: Double = 1.0,
    ): Double

    fun getMaxHit(
        pawn: Pawn,
        target: Pawn,
        specialAttackMultiplier: Double = 1.0,
        specialPassiveMultiplier: Double = 1.0,
    ): Int
}
