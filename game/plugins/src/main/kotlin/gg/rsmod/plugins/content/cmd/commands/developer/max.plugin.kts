package gg.rsmod.plugins.content.cmd.commands.developer

import gg.rsmod.game.model.combat.CombatClass
import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.combat.CombatConfigs
import gg.rsmod.plugins.content.combat.formula.MagicCombatFormula
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.formula.RangedCombatFormula
import gg.rsmod.plugins.content.combat.getCombatTarget

on_command("max", Privilege.DEV_POWER, description = "Max hit") {
    val target = player.getCombatTarget() ?: player
    CombatClass.values.forEach { combatClass ->
        val max: Int
        val accuracy: Double
        when (combatClass) {
            CombatClass.MAGIC -> {
                accuracy = MagicCombatFormula.getAccuracy(player, target)
                max = MagicCombatFormula.getMaxHit(player, target)
            }
            CombatClass.RANGED -> {
                accuracy = RangedCombatFormula.getAccuracy(player, target)
                max = RangedCombatFormula.getMaxHit(player, target)
            }
            CombatClass.MELEE -> {
                accuracy = MeleeCombatFormula.getAccuracy(player, target)
                max = MeleeCombatFormula.getMaxHit(player, target)
            }
            else -> throw IllegalStateException("Unhandled combat class: ${CombatConfigs.getCombatClass(player)} for $player")
        }
        val name = combatClass.name.toLowerCase().capitalize()
        val message = """<col=178000>$name:</col>   Max hit=<col=801700>$max</col>   Accuracy=<col=801700>${(accuracy * 100).toInt()}%</col> [${String.format("%.6f", accuracy)}]"""
        player.message(message)
    }
}