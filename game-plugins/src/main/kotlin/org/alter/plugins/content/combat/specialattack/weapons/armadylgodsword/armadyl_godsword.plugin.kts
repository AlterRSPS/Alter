package org.alter.plugins.content.combat.specialattack.weapons.armadylgodsword

import org.alter.game.model.entity.AreaSound
import org.alter.plugins.content.combat.dealHit
import org.alter.plugins.content.combat.formula.MeleeCombatFormula
import org.alter.plugins.content.combat.specialattack.SpecialAttacks


val SPECIAL_REQUIREMENT = 50

SpecialAttacks.register("item.armadyl_godsword", SPECIAL_REQUIREMENT) {
    player.animate(id = 7644)
    player.graphic(id = 1211)

    world.spawn(AreaSound(tile = player.tile, id = 3869, radius = 10, volume = 1))

    val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.375)
    val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 2.0)
    val landHit = accuracy >= world.randomDouble()
    val delay = 1
    player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = delay)
}

SpecialAttacks.register("item.armadyl_godsword_or", SPECIAL_REQUIREMENT) {
    player.animate(id = 7644)
    player.graphic(id = 1211)
    world.spawn(AreaSound(tile = player.tile, id = 3869, radius = 10, volume = 1))

    val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.375)
    val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 2.0)
    val landHit = accuracy >= world.randomDouble()
    val delay = 1
    player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = delay)
}
