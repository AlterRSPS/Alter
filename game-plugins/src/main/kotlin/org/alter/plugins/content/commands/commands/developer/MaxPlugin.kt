package org.alter.plugins.content.commands.commands.developer

import org.alter.api.*
import org.alter.api.cfg.*
import org.alter.api.dsl.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.attr.*
import org.alter.game.model.combat.CombatClass
import org.alter.game.model.container.*
import org.alter.game.model.container.key.*
import org.alter.game.model.entity.*
import org.alter.game.model.item.*
import org.alter.game.model.priv.Privilege
import org.alter.game.model.queue.*
import org.alter.game.model.shop.*
import org.alter.game.model.timer.*
import org.alter.game.plugin.*
import org.alter.plugins.content.combat.CombatConfigs
import org.alter.plugins.content.combat.formula.MagicCombatFormula
import org.alter.plugins.content.combat.formula.MeleeCombatFormula
import org.alter.plugins.content.combat.formula.RangedCombatFormula
import org.alter.plugins.content.combat.getCombatTarget

class MaxPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onCommand("max", Privilege.DEV_POWER, description = "Max hit") {
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
                val name = combatClass.name.lowercase()
                val message = """<col=178000>$name:</col>   Max hit=<col=801700>$max</col>   Accuracy=<col=801700>${(accuracy * 100).toInt()}%</col> [${String.format(
                    "%.6f",
                    accuracy,
                )}]"""
                player.message(message)
            }
        }
    }
}
