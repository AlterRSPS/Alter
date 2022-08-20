package gg.rsmod.plugins.content.combat.specialattack.weapons.armadylgodsword

import gg.rsmod.game.model.attr.COMBAT_TARGET_FOCUS_ATTR
import gg.rsmod.game.model.entity.AreaSound
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.getAttackStyle
import gg.rsmod.plugins.api.ext.pawn
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

private val AGS = intArrayOf(Items.ARMADYL_GODSWORD, Items.ARMADYL_GODSWORD_OR)

val SPECIAL_REQUIREMENT = 50

SpecialAttacks.register(Items.ARMADYL_GODSWORD, SPECIAL_REQUIREMENT) {
    player.animate(id = 7644)
    player.graphic(id = 1211)

    world.spawn(AreaSound(tile = player.tile, id = 3869, radius = 10, volume = 1))


    val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.375)
    val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 2.0)
    val landHit = accuracy >= world.randomDouble()
    val delay = 1
    player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = delay)

}

SpecialAttacks.register(Items.ARMADYL_GODSWORD_OR, SPECIAL_REQUIREMENT) {
    player.animate(id = 7644)
    player.graphic(id = 1211)
    world.spawn(AreaSound(tile = player.tile, id = 3869, radius = 10, volume = 1))


    val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.375)
    val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 2.0)
    val landHit = accuracy >= world.randomDouble()
    val delay = 1
    player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = delay)

}