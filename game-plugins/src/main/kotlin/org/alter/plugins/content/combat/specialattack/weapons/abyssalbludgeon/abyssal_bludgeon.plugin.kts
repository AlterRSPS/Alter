package org.alter.plugins.content.combat.specialattack.weapons.abyssalbludgeon

import org.alter.api.Skills
import org.alter.api.cfg.Graphic
import org.alter.game.model.entity.AreaSound
import org.alter.plugins.content.combat.dealHit
import org.alter.plugins.content.combat.formula.MeleeCombatFormula
import org.alter.plugins.content.combat.specialattack.SpecialAttacks

val SPECIAL_REQUIREMENT = 50

SpecialAttacks.register("item.abyssal_bludgeon", SPECIAL_REQUIREMENT) {
    player.animate(id = 3299)
    player.graphic(id = 1284)

    world.spawn(AreaSound(tile = player.tile, id = 2715, radius = 10, volume = 1, delay = 10))
    world.spawn(AreaSound(tile = player.tile, id = 1930, radius = 10, volume = 1, delay = 30))

    val dmgBonus = (player.getSkills().getBaseLevel(Skills.PRAYER) - player.getSkills().getCurrentLevel(Skills.PRAYER)) * .5 / 100
    val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = dmgBonus)
    val landHit = MeleeCombatFormula.getAccuracy(player, target) >= world.randomDouble()
    player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = 1)
}

setItemCombatLogic("item.abyssal_bludgeon") {
    val target = player.getTarget()
    if (target != null) {
        target.graphic(Graphic.ABYSSAL_BLUDGEON_SPECIAL)
        player.dealHit(target = target, maxHit = 10, landHit = true, delay = 1)
    }
}