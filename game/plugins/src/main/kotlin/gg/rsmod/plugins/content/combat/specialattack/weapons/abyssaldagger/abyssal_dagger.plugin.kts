package gg.rsmod.plugins.content.combat.specialattack.weapons.abyssaldagger

import gg.rsmod.game.model.entity.AreaSound
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks
import kotlin.math.roundToInt

val SPECIAL_REQUIREMENT = 50

SpecialAttacks.register(Items.ABYSSAL_DAGGER, SPECIAL_REQUIREMENT) {
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

SpecialAttacks.register(Items.ABYSSAL_DAGGER_P, SPECIAL_REQUIREMENT) {
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

SpecialAttacks.register(Items.ABYSSAL_DAGGER_P_13271, SPECIAL_REQUIREMENT) {
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

SpecialAttacks.register(Items.ABYSSAL_DAGGER_P_13269, SPECIAL_REQUIREMENT) {
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