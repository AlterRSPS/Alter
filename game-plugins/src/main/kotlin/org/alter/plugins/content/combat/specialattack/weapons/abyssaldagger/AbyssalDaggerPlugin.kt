package org.alter.plugins.content.combat.specialattack.weapons.abyssaldagger

import org.alter.plugins.content.combat.dealHit
import org.alter.plugins.content.combat.formula.MeleeCombatFormula
import org.alter.plugins.content.combat.specialattack.SpecialAttacks
import kotlin.math.roundToInt

val SPECIAL_REQUIREMENT = 50

for(item in listOf(
    "item.abyssal_dagger",
    "item.abyssal_dagger_p",
    "item.abyssal_dagger_p_13269",
    "item.abyssal_dagger_p_13271"
)) {
    SpecialAttacks.register(item, SPECIAL_REQUIREMENT) {
        player.animate(id = 3300)
        player.graphic(id = 1283)
        world.spawn(AreaSound(tile = player.tile, id = 2537, radius = 10, volume = 1))

        val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 1.25)
        val landHit = accuracy >= world.randomDouble()

        for (i in 0 until 2) {
            val maxHit = MeleeCombatFormula.getMaxHit(player, target) * .85
            player.dealHit(target = target, maxHit = maxHit.roundToInt(), landHit = landHit, delay = 1)
        }
    }
}