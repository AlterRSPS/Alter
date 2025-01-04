package org.alter.plugins.content.combat.formula

import org.alter.api.*
import org.alter.api.ext.*
import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.plugins.content.combat.Combat
import org.alter.plugins.content.combat.CombatConfigs
import org.alter.plugins.content.combat.strategy.magic.CombatSpell
import org.alter.plugins.content.mechanics.prayer.Prayer
import org.alter.plugins.content.mechanics.prayer.Prayers

/**
 * @author Tom <rspsmods@gmail.com>
 */
object MagicCombatFormula : CombatFormula {
    private val BLACK_MASKS =
        arrayOf(
            "item.black_mask",
            "item.black_mask_1",
            "item.black_mask_2",
            "item.black_mask_3",
            "item.black_mask_4",
            "item.black_mask_5",
            "item.black_mask_6",
            "item.black_mask_7",
            "item.black_mask_8",
            "item.black_mask_9",
            "item.black_mask_10",
        )

    private val BLACK_MASKS_I =
        arrayOf(
            "item.black_mask_i",
            "item.black_mask_1_i",
            "item.black_mask_2_i",
            "item.black_mask_3_i",
            "item.black_mask_4_i",
            "item.black_mask_5_i",
            "item.black_mask_6_i",
            "item.black_mask_7_i",
            "item.black_mask_8_i",
            "item.black_mask_9_i",
            "item.black_mask_10_i",
        )

    private val SLAYER_HELM_I =
        arrayOf(
            "item.slayer_helmet_i",
            "item.black_slayer_helmet_i",
            "item.green_slayer_helmet_i",
            "item.purple_slayer_helmet_i",
            "item.red_slayer_helmet_i",
            "item.turquoise_slayer_helmet_i",
        )

    private val MAGE_VOID = arrayOf("item.void_mage_helm", "item.void_knight_top", "item.void_knight_robe", "item.void_knight_gloves")

    private val MAGE_ELITE_VOID = arrayOf("item.void_mage_helm", "item.elite_void_top", "item.elite_void_robe", "item.void_knight_gloves")

    private val BOLT_SPELLS = enumSetOf(CombatSpell.WIND_BOLT, CombatSpell.WATER_BOLT, CombatSpell.EARTH_BOLT, CombatSpell.FIRE_BOLT)

    private val FIRE_SPELLS =
        enumSetOf(CombatSpell.FIRE_STRIKE, CombatSpell.FIRE_BOLT, CombatSpell.FIRE_BLAST, CombatSpell.FIRE_WAVE, CombatSpell.FIRE_SURGE)

    override fun getAccuracy(
        pawn: Pawn,
        target: Pawn,
        specialAttackMultiplier: Double,
    ): Double {
        val attack = getAttackRoll(pawn)
        val defence =
            if (target is Player) {
                getDefenceRoll(target)
            } else if (target is Npc) {
                getDefenceRoll(pawn, target)
            } else {
                throw IllegalArgumentException("Unhandled pawn.")
            }

        val accuracy: Double
        if (attack > defence) {
            accuracy = 1.0 - (defence + 2.0) / (2.0 * (attack + 1.0))
        } else {
            accuracy = attack / (2.0 * (defence + 1))
        }
        return accuracy
    }

    override fun getMaxHit(
        pawn: Pawn,
        target: Pawn,
        specialAttackMultiplier: Double,
        specialPassiveMultiplier: Double,
    ): Int {
        val spell = pawn.attr[Combat.CASTING_SPELL]
        var hit = spell?.maxHit?.toDouble() ?: 1.0
        if (pawn is Player) {
            val magic = pawn.getSkills().getCurrentLevel(Skills.MAGIC)
            if (pawn.hasEquipped(
                    EquipmentType.WEAPON,
                    "item.trident_of_the_seas",
                    "item.trident_of_the_seas_e",
                    "item.trident_of_the_seas_full",
                )
            ) {
                hit = (Math.floor(magic / 3.0) - 5.0)
            } else if (pawn.hasEquipped(EquipmentType.WEAPON, "item.trident_of_the_swamp", "item.trident_of_the_swamp_e")) {
                hit = (Math.floor(magic / 3.0) - 2.0)
            }

            if (pawn.hasEquipped(EquipmentType.GLOVES, "item.chaos_gauntlets") && spell != null && spell in BOLT_SPELLS) {
                hit += 3
            }

            var multiplier = 1.0 + (pawn.getMagicDamageBonus() / 100.0)

            if (pawn.hasEquipped(
                    EquipmentType.AMULET,
                    "item.amulet_of_the_damned_full",
                ) &&
                pawn.hasEquipped(
                    EquipmentType.WEAPON,
                    "item.ahrims_staff",
                    "item.ahrims_staff_25",
                    "item.ahrims_staff_50",
                    "item.ahrims_staff_75",
                    "item.ahrims_staff_100",
                ) &&
                pawn.world.chance(1, 4)
            ) {
                multiplier += 0.3
            }

            if (pawn.hasEquipped(EquipmentType.WEAPON, "item.mystic_smoke_staff") && pawn.hasSpellbook(Spellbook.NORMAL)) {
                multiplier += 0.1
            }

            if (pawn.hasEquipped(MAGE_ELITE_VOID)) {
                multiplier += 0.025
            }

            hit *= multiplier
            hit = Math.floor(hit)

            if (pawn.hasEquipped(EquipmentType.SHIELD, "item.tome_of_fire") && spell in FIRE_SPELLS) {
                // TODO: check tome of fire has charges
                hit *= 1.5
                hit = Math.floor(hit)
            }

            if (target is Npc) {
                if (pawn.hasEquipped(EquipmentType.HEAD, *BLACK_MASKS_I) || pawn.hasEquipped(EquipmentType.HEAD, *SLAYER_HELM_I)) {
                    // TODO: check if on slayer task and target is slayer task
                    hit *= 1.15
                    hit = Math.floor(hit)
                } else if (pawn.hasEquipped(EquipmentType.AMULET, "item.salve_amuletei") && target.isSpecies(NpcSpecies.UNDEAD)) {
                    hit *= 1.20
                    hit = Math.floor(hit)
                }
            }
        } else if (pawn is Npc) {
            val multiplier = 1.0 + (pawn.getMagicDamageBonus() / 100.0)
            hit *= multiplier
            hit = Math.floor(hit)
        }

        hit *= getDamageDealMultiplier(pawn)
        hit = Math.floor(hit)

        return hit.toInt()
    }

    private fun getAttackRoll(pawn: Pawn): Int {
        val a =
            if (pawn is Player) {
                getEffectiveAttackLevel(pawn)
            } else if (pawn is Npc) {
                getEffectiveAttackLevel(pawn)
            } else {
                0.0
            }
        val b = getEquipmentAttackBonus(pawn)

        var maxRoll = a * (b + 64.0)
        if (pawn is Player) {
            maxRoll = applyAttackSpecials(pawn, maxRoll)
        }
        return maxRoll.toInt()
    }

    private fun getDefenceRoll(
        pawn: Pawn,
        target: Npc,
    ): Int {
        val a =
            if (pawn is Player) {
                getEffectiveDefenceLevel(pawn)
            } else if (pawn is Npc) {
                getEffectiveDefenceLevel(pawn)
            } else {
                0.0
            }
        val b = getEquipmentDefenceBonus(target)

        val maxRoll = a * (b + 64.0)
        return maxRoll.toInt()
    }

    private fun getDefenceRoll(target: Player): Int {
        var effectiveLvl = getEffectiveDefenceLevel(target)

        effectiveLvl *= 0.3
        effectiveLvl = Math.floor(effectiveLvl)

        var magicLvl = target.getSkills().getCurrentLevel(Skills.MAGIC).toDouble()
        magicLvl *= getPrayerAttackMultiplier(target)
        magicLvl = Math.floor(magicLvl)

        magicLvl *= 0.7
        magicLvl = Math.floor(magicLvl)

        val a = Math.floor(effectiveLvl + magicLvl).toInt()
        val b = getEquipmentDefenceBonus(target)

        val maxRoll = a * (b + 64.0)
        return maxRoll.toInt()
    }

    private fun applyAttackSpecials(
        player: Player,
        base: Double,
    ): Double {
        var hit = base

        hit *= getEquipmentMultiplier(player)
        hit = Math.floor(hit)

        if (player.hasEquipped(EquipmentType.WEAPON, "item.mystic_smoke_staff")) {
            hit *= 1.1
            hit = Math.floor(hit)
        }

        return hit
    }

    private fun getEffectiveAttackLevel(player: Player): Double {
        var effectiveLevel = Math.floor(player.getSkills().getCurrentLevel(Skills.MAGIC) * getPrayerAttackMultiplier(player))

        if (player.hasWeaponType(WeaponType.TRIDENT)) {
            effectiveLevel +=
                when (CombatConfigs.getAttackStyle(player)) {
                    AttackStyle.ACCURATE -> 3.0
                    AttackStyle.CONTROLLED -> 1.0
                    else -> 0.0
                }
        }

        effectiveLevel += 8.0

        if (player.hasEquipped(MAGE_VOID) || player.hasEquipped(MAGE_ELITE_VOID)) {
            effectiveLevel *= 1.45
            effectiveLevel = Math.floor(effectiveLevel)
        }

        return Math.floor(effectiveLevel)
    }

    private fun getEffectiveDefenceLevel(player: Player): Double {
        var effectiveLevel = Math.floor(player.getSkills().getCurrentLevel(Skills.DEFENCE) * getPrayerDefenceMultiplier(player))

        effectiveLevel +=
            when (CombatConfigs.getAttackStyle(player)) {
                AttackStyle.DEFENSIVE -> 3.0
                AttackStyle.CONTROLLED -> 1.0
                AttackStyle.LONG_RANGE -> 3.0
                else -> 0.0
            }

        effectiveLevel += 8.0

        return Math.floor(effectiveLevel)
    }

    private fun getEffectiveAttackLevel(npc: Npc): Double {
        var effectiveLevel = npc.stats.getCurrentLevel(NpcSkills.MAGIC).toDouble()
        effectiveLevel += 8
        return effectiveLevel
    }

    private fun getEffectiveDefenceLevel(npc: Npc): Double {
        var effectiveLevel = npc.stats.getCurrentLevel(NpcSkills.DEFENCE).toDouble()
        effectiveLevel += 8
        return effectiveLevel
    }

    private fun getEquipmentAttackBonus(pawn: Pawn): Double {
        return pawn.getBonus(BonusSlot.ATTACK_MAGIC).toDouble()
    }

    private fun getEquipmentDefenceBonus(target: Pawn): Double {
        return target.getBonus(BonusSlot.DEFENCE_MAGIC).toDouble()
    }

    private fun getEquipmentMultiplier(player: Player): Double =
        when {
            player.hasEquipped(EquipmentType.AMULET, "item.salve_amulet") -> 7.0 / 6.0
            player.hasEquipped(EquipmentType.AMULET, "item.salve_amulet_e") -> 1.2
            player.hasEquipped(EquipmentType.AMULET, "item.salve_amuleti") -> 1.15
            player.hasEquipped(EquipmentType.AMULET, "item.salve_amuletei") -> 1.2
            // TODO: this should only apply when target is slayer task?
            player.hasEquipped(EquipmentType.HEAD, *BLACK_MASKS) -> 7.0 / 6.0
            player.hasEquipped(EquipmentType.HEAD, *BLACK_MASKS_I) -> 1.15
            else -> 1.0
        }

    private fun getPrayerAttackMultiplier(player: Player): Double =
        when {
            Prayers.isActive(player, Prayer.MYSTIC_WILL) -> 1.05
            Prayers.isActive(player, Prayer.MYSTIC_LORE) -> 1.10
            Prayers.isActive(player, Prayer.MYSTIC_MIGHT) -> 1.15
            Prayers.isActive(player, Prayer.AUGURY) -> 1.25
            else -> 1.0
        }

    private fun getPrayerDefenceMultiplier(player: Player): Double =
        when {
            Prayers.isActive(player, Prayer.THICK_SKIN) -> 1.05
            Prayers.isActive(player, Prayer.ROCK_SKIN) -> 1.10
            Prayers.isActive(player, Prayer.STEEL_SKIN) -> 1.15
            Prayers.isActive(player, Prayer.CHIVALRY) -> 1.20
            Prayers.isActive(player, Prayer.PIETY) -> 1.25
            Prayers.isActive(player, Prayer.RIGOUR) -> 1.25
            Prayers.isActive(player, Prayer.AUGURY) -> 1.25
            else -> 1.0
        }

    private fun getDamageDealMultiplier(pawn: Pawn): Double = pawn.attr[Combat.DAMAGE_DEAL_MULTIPLIER] ?: 1.0
}
