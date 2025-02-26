package org.alter.plugins.content.mechanics.run

import org.alter.api.EquipmentType
import org.alter.api.Skills
import org.alter.api.cfg.Varp
import org.alter.api.ext.*
import org.alter.game.model.bits.INFINITE_VARS_STORAGE
import org.alter.game.model.bits.InfiniteVarsType
import org.alter.game.model.entity.Player
import org.alter.game.model.move.hasMoveDestination
import org.alter.game.model.timer.TimerKey
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

/**
 * @author Tom <rspsmods@gmail.com>
 */
object RunEnergy {
    val RUN_DRAIN = TimerKey()

    /**
     * Reduces run energy depletion by 70%
     */
    val STAMINA_BOOST = TimerKey("stamina_boost", tickOffline = false)

    const val RUN_ENABLED_VARP = Varp.RUN_MODE_VARP
    
    // Graceful set bonus constants
    private const val GRACEFUL_RESTORE_PER_PIECE = 0.03 // 3% per piece
    private const val GRACEFUL_FULL_SET_BONUS = 0.10 // 10% additional for full set
    private const val GRACEFUL_FULL_SET_EXTRA_BONUS = 0.20 // 20% more for full set (total 30%)

    fun toggle(p: Player) {
        if (p.runEnergy >= 100.0) {
            p.toggleVarp(RUN_ENABLED_VARP)
        } else {
            p.setVarp(RUN_ENABLED_VARP, 0)
            p.message("You don't have enough run energy left.")
        }
    }

    fun drain(p: Player) {
        if (p.isRunning() && p.hasMoveDestination()) {
            if (!p.hasStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.RUN)) {
                val weight = max(0.0, p.weight)
                val agilityLevel = p.getSkills().getCurrentLevel(Skills.AGILITY)
                
                // OSRS Jan 2025 update:
                // Drain Rate formula: (60 + (67 * Weight / 64)) * (1 - Agility/300)
                // This is energy lost per tick
                var drainPerTick = (60.0 + (67.0 * weight / 64.0)) * (1.0 - agilityLevel / 300.0)
                
                if (p.timers.has(STAMINA_BOOST)) {
                    drainPerTick *= 0.3
                }
                
                // Apply graceful pieces drain reduction if applicable
                val gracefulPiecesWorn = countGracefulPiecesWorn(p)
                if (gracefulPiecesWorn > 0) {
                    // Apply drain reduction per piece
                    val reductionFactor = 1.0 - (gracefulPiecesWorn * GRACEFUL_RESTORE_PER_PIECE)
                    drainPerTick *= reductionFactor
                    
                    // Apply full set bonus if applicable
                    if (gracefulPiecesWorn == 6) {
                        drainPerTick *= (1.0 - GRACEFUL_FULL_SET_BONUS - GRACEFUL_FULL_SET_EXTRA_BONUS)
                    }
                }
                
                p.runEnergy = max(0.0, p.runEnergy - drainPerTick)
                if (p.runEnergy <= 0) {
                    p.varps.setState(RUN_ENABLED_VARP, 0)
                }
                p.sendRunEnergy(p.runEnergy.toInt())
            }
        } else if (p.runEnergy < 10000.0 && p.lock.canRestoreRunEnergy()) {
            val agilityLevel = p.getSkills().getCurrentLevel(Skills.AGILITY)
            
            // OSRS Jan 2025 update:
            // Regeneration formula: (Agility level / 10) + 15
            // This is energy gained per tick
            var regenPerTick = floor((agilityLevel / 10.0) + 15.0)
            
            // Apply graceful bonuses
            val gracefulPiecesWorn = countGracefulPiecesWorn(p)
            if (gracefulPiecesWorn > 0) {
                // Apply per-piece bonus
                regenPerTick *= (1.0 + (gracefulPiecesWorn * GRACEFUL_RESTORE_PER_PIECE))
                
                // Apply full set bonus if applicable
                if (gracefulPiecesWorn == 6) {
                    regenPerTick *= (1.0 + GRACEFUL_FULL_SET_BONUS + GRACEFUL_FULL_SET_EXTRA_BONUS)
                }
            }
            
            // Apply recovery rate (the OSRS scaling is 0-10000 for internal energy)
            p.runEnergy = min(10000.0, p.runEnergy + regenPerTick)
            p.sendRunEnergy(p.runEnergy.toInt())
        }
    }

    private fun countGracefulPiecesWorn(p: Player): Int {
        var count = 0
        
        if ((p.equipment[EquipmentType.HEAD.id]?.id ?: -1) in GRACEFUL_HOODS) count++
        if ((p.equipment[EquipmentType.CAPE.id]?.id ?: -1) in GRACEFUL_CAPE) count++
        if ((p.equipment[EquipmentType.CHEST.id]?.id ?: -1) in GRACEFUL_TOP) count++
        if ((p.equipment[EquipmentType.LEGS.id]?.id ?: -1) in GRACEFUL_LEGS) count++
        if ((p.equipment[EquipmentType.GLOVES.id]?.id ?: -1) in GRACEFUL_GLOVES) count++
        if ((p.equipment[EquipmentType.BOOTS.id]?.id ?: -1) in GRACEFUL_BOOTS) count++
        
        return count
    }

    private fun isWearingFullGrace(p: Player): Boolean =
        countGracefulPiecesWorn(p) == 6


    /**
     * @TODO Magic Numbers
     */
    private val GRACEFUL_HOODS = intArrayOf(11850, 13579, 13591, 13603, 13615, 13627, 13667, 21061)

    private val GRACEFUL_CAPE = intArrayOf(11852, 13581, 13593, 13605, 13617, 13629, 13669, 21064)

    private val GRACEFUL_TOP = intArrayOf(11854, 13583, 13595, 13607, 13619, 13631, 13671, 21067)

    private val GRACEFUL_LEGS = intArrayOf(11856, 13585, 13597, 13609, 13621, 13633, 13673, 21070)

    private val GRACEFUL_GLOVES = intArrayOf(11858, 13587, 13599, 13611, 13623, 13635, 13675, 21073)

    private val GRACEFUL_BOOTS = intArrayOf(11860, 13589, 13601, 13613, 13625, 13637, 13677, 21076)
}
