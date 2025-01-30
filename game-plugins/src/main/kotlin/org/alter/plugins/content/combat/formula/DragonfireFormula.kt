package org.alter.plugins.content.combat.formula

import org.alter.api.EquipmentType
import org.alter.api.NpcSpecies
import org.alter.api.PrayerIcon
import org.alter.api.ext.hasEquipped
import org.alter.api.ext.hasPrayerIcon
import org.alter.api.ext.isSpecies
import org.alter.game.model.attr.ANTIFIRE_POTION_CHARGES_ATTR
import org.alter.game.model.attr.DRAGONFIRE_IMMUNITY_ATTR
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DragonfireFormula(val maxHit: Int, val minHit: Int = 0) : CombatFormula {
    override fun getAccuracy(
        pawn: Pawn,
        target: Pawn,
        specialAttackMultiplier: Double,
    ): Double {
        return MagicCombatFormula.getAccuracy(pawn, target, specialAttackMultiplier)
    }

    override fun getMaxHit(
        pawn: Pawn,
        target: Pawn,
        specialAttackMultiplier: Double,
        specialPassiveMultiplier: Double,
    ): Int {
        var max = maxHit.toDouble()

        if (target is Player) {
            val magicProtection = target.hasPrayerIcon(PrayerIcon.PROTECT_FROM_MAGIC)
            val antiFirePotion = (target.attr[ANTIFIRE_POTION_CHARGES_ATTR] ?: 0) > 0
            val dragonFireImmunity = target.attr[DRAGONFIRE_IMMUNITY_ATTR] ?: false
            val antiFireShield = target.hasEquipped(EquipmentType.SHIELD, *ANTI_DRAGON_SHIELDS)
            val dragonfireShield = target.hasEquipped(EquipmentType.SHIELD, *DRAGONFIRE_SHIELDS)
            val wyvernShield = target.hasEquipped(EquipmentType.SHIELD, *WYVERN_SHIELDS)
            val dragonfireWard = target.hasEquipped(EquipmentType.SHIELD, *DRAGONFIRE_WARDS)

            if (dragonFireImmunity) {
                return minHit
            }

            if (pawn is Npc) {
                val basicDragon = pawn.isSpecies(NpcSpecies.BASIC_DRAGON)
                val brutalDragon = pawn.isSpecies(NpcSpecies.BRUTAL_DRAGON)

                if (magicProtection && basicDragon) {
                    max *= 0.35
                } else if (magicProtection && brutalDragon && antiFirePotion) {
                    return minHit
                }
            }

            if (antiFireShield || dragonfireShield || wyvernShield || dragonfireWard) {
                if (!antiFirePotion) {
                    max *= 0.25
                } else {
                    return minHit
                }
            }

            if (antiFirePotion) {
                max *= 0.20
            }
        }

        return Math.max(minHit, Math.floor(max).toInt())
    }

    companion object {
        private val ANTI_DRAGON_SHIELDS = arrayOf("item.antidragon_shield", "item.antidragon_shield_nz")
        private val DRAGONFIRE_SHIELDS = arrayOf("item.dragonfire_shield", "item.dragonfire_shield_11284")
        private val WYVERN_SHIELDS = arrayOf("item.ancient_wyvern_shield", "item.ancient_wyvern_shield_21634") // Does [Items.ANCIENT_WYVERN_SHIELD] Uncharged even protect u from Dragon Fire ?
        private val DRAGONFIRE_WARDS = arrayOf("item.dragonfire_ward", "item.dragonfire_ward_22003")
    }
}
