package org.alter.plugins.content.combat.formula

import org.alter.api.*
import org.alter.api.ext.*
import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.plugins.content.combat.Combat
import org.alter.plugins.content.combat.CombatConfigs
import org.alter.plugins.content.mechanics.prayer.Prayer
import org.alter.plugins.content.mechanics.prayer.Prayers

/**
 * @author Tom <rspsmods@gmail.com>
 */
object RangedCombatFormula : CombatFormula {
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

    private val RANGED_VOID = arrayOf("item.void_ranger_helm", "item.void_knight_top", "item.void_knight_robe", "item.void_knight_gloves")

    private val RANGED_ELITE_VOID =
        arrayOf("item.void_ranger_helm", "item.elite_void_top", "item.elite_void_robe", "item.void_knight_gloves")

    override fun getAccuracy(
        pawn: Pawn,
        target: Pawn,
        specialAttackMultiplier: Double,
    ): Double {
        val attack = getAttackRoll(pawn, target, specialAttackMultiplier)
        val defence = getDefenceRoll(pawn, target)

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
        val a =
            if (pawn is Player) {
                getEffectiveRangedLevel(pawn)
            } else if (pawn is Npc) {
                getEffectiveRangedLevel(pawn)
            } else {
                0.0
            }
        val b = getEquipmentRangedBonus(pawn)

        var base = Math.floor(0.5 + a * (b + 64.0) / 640.0).toInt()
        if (pawn is Player) {
            base = applyRangedSpecials(pawn, target, base, specialAttackMultiplier, specialPassiveMultiplier)
        }
        return base
    }

    private fun getAttackRoll(
        pawn: Pawn,
        target: Pawn,
        specialAttackMultiplier: Double,
    ): Int {
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
            maxRoll = applyAttackSpecials(pawn, target, maxRoll, specialAttackMultiplier)
        }
        return maxRoll.toInt()
    }

    private fun getDefenceRoll(
        pawn: Pawn,
        target: Pawn,
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

        var maxRoll = a * (b + 64.0)
        maxRoll = applyDefenceSpecials(target, maxRoll)
        return maxRoll.toInt()
    }

    private fun applyRangedSpecials(
        player: Player,
        target: Pawn,
        base: Int,
        specialAttackMultiplier: Double,
        specialPassiveMultiplier: Double,
    ): Int {
        var hit = base.toDouble()

        hit *= getEquipmentMultiplier(player)
        hit = Math.floor(hit)

        if (specialAttackMultiplier == 1.0) {
            val multiplier =
                when {
                    player.hasEquipped(EquipmentType.WEAPON, "item.dragon_hunter_crossbow") && isDragon(target) -> 1.3
                    player.hasEquipped(EquipmentType.WEAPON, "item.twisted_bow") && target.entityType.isNpc -> {
                        // TODO: cap inside Chambers of Xeric is 350
                        val cap = 250.0
                        val magic =
                            when (target) {
                                is Player -> target.getSkills().getCurrentLevel(Skills.MAGIC)
                                is Npc -> target.stats.getCurrentLevel(NpcSkills.MAGIC)
                                else -> throw IllegalStateException("Invalid pawn type. [$target]")
                            }
                        val modifier =
                            Math.min(
                                cap,
                                250.0 + (((magic * 3.0) - 14.0) / 100.0) - (Math.pow((((magic * 3.0) / 10.0) - 140.0), 2.0) / 100.0),
                            )
                        modifier
                    }
                    else -> 1.0
                }
            hit *= multiplier
            hit = Math.floor(hit)
        } else {
            hit *= specialAttackMultiplier
            hit = Math.floor(hit)
        }

        if (target.hasPrayerIcon(PrayerIcon.PROTECT_FROM_MISSILES)) {
            hit *= 0.6
            hit = Math.floor(hit)
        }

        if (specialPassiveMultiplier == 1.0) {
            hit = applyPassiveMultiplier(player, target, hit)
            hit = Math.floor(hit)
        } else {
            hit *= specialPassiveMultiplier
            hit = Math.floor(hit)
        }

        hit *= getDamageDealMultiplier(player)
        hit = Math.floor(hit)

        hit *= getDamageTakeMultiplier(target)
        hit = Math.floor(hit)

        return hit.toInt()
    }

    private fun applyAttackSpecials(
        player: Player,
        target: Pawn,
        base: Double,
        specialAttackMultiplier: Double,
    ): Double {
        var hit = base

        hit *= getEquipmentMultiplier(player)
        hit = Math.floor(hit)

        if (specialAttackMultiplier == 1.0) {
            val multiplier =
                when {
                    player.hasEquipped(EquipmentType.WEAPON, "item.dragon_hunter_crossbow") && isDragon(target) -> 1.3
                    player.hasEquipped(EquipmentType.WEAPON, "item.twisted_bow") && target.entityType.isNpc -> {
                        // TODO: cap inside Chambers of Xeric is 250
                        val cap = 140.0
                        val magic =
                            when (target) {
                                is Player -> target.getSkills().getCurrentLevel(Skills.MAGIC)
                                is Npc -> target.stats.getCurrentLevel(NpcSkills.MAGIC)
                                else -> throw IllegalStateException("Invalid pawn type. [$target]")
                            }
                        val modifier =
                            Math.min(
                                cap,
                                140.0 + (((magic * 3.0) - 10.0) / 100.0) - (Math.pow((((magic * 3.0) / 10.0) - 100.0), 2.0) / 100.0),
                            )
                        modifier
                    }
                    else -> 1.0
                }
            hit *= multiplier
            hit = Math.floor(hit)
        } else {
            hit *= specialAttackMultiplier
            hit = Math.floor(hit)
        }

        return hit
    }

    private fun applyDefenceSpecials(
        target: Pawn,
        base: Double,
    ): Double {
        var hit = base

        if (target is Player && isWearingTorag(target) && target.hasEquipped(EquipmentType.AMULET, "item.amulet_of_the_damned_full")) {
            val lost = (target.getMaxHp() - target.getCurrentHp()) / 100.0
            val max = target.getMaxHp() / 100.0
            hit *= (1.0 + (lost * max))
            hit = Math.floor(hit)
        }

        return hit
    }

    private fun getEquipmentRangedBonus(pawn: Pawn): Double =
        when (pawn) {
            is Player -> pawn.getRangedStrengthBonus().toDouble()
            is Npc -> pawn.getRangedStrengthBonus().toDouble()
            else -> throw IllegalArgumentException("Invalid pawn type. $pawn")
        }

    private fun getEquipmentAttackBonus(pawn: Pawn): Double {
        return pawn.getBonus(BonusSlot.ATTACK_RANGED).toDouble()
    }

    private fun getEquipmentDefenceBonus(target: Pawn): Double {
        return target.getBonus(BonusSlot.DEFENCE_RANGED).toDouble()
    }

    private fun getEffectiveRangedLevel(player: Player): Double {
        var effectiveLevel = Math.floor(player.getSkills().getCurrentLevel(Skills.RANGED) * getPrayerRangedMultiplier(player))

        effectiveLevel +=
            when (CombatConfigs.getAttackStyle(player)) {
                AttackStyle.ACCURATE -> 3.0
                else -> 0.0
            }

        effectiveLevel += 8.0

        if (player.hasEquipped(RANGED_VOID)) {
            effectiveLevel *= 1.10
            effectiveLevel = Math.floor(effectiveLevel)
        } else if (player.hasEquipped(RANGED_ELITE_VOID)) {
            effectiveLevel *= 1.125
            effectiveLevel = Math.floor(effectiveLevel)
        }

        return Math.floor(effectiveLevel)
    }

    private fun getEffectiveAttackLevel(player: Player): Double {
        var effectiveLevel = Math.floor(player.getSkills().getCurrentLevel(Skills.RANGED) * getPrayerAttackMultiplier(player))

        effectiveLevel +=
            when (CombatConfigs.getAttackStyle(player)) {
                AttackStyle.ACCURATE -> 3.0
                else -> 0.0
            }

        effectiveLevel += 8.0

        if (player.hasEquipped(RANGED_VOID) || player.hasEquipped(RANGED_ELITE_VOID)) {
            effectiveLevel *= 1.10
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

    private fun getEffectiveRangedLevel(npc: Npc): Double {
        var effectiveLevel = npc.stats.getCurrentLevel(NpcSkills.RANGED).toDouble()
        effectiveLevel += 8
        return effectiveLevel
    }

    private fun getEffectiveAttackLevel(npc: Npc): Double {
        var effectiveLevel = npc.stats.getCurrentLevel(NpcSkills.RANGED).toDouble()
        effectiveLevel += 8
        return effectiveLevel
    }

    private fun getEffectiveDefenceLevel(npc: Npc): Double {
        var effectiveLevel = npc.stats.getCurrentLevel(NpcSkills.DEFENCE).toDouble()
        effectiveLevel += 8
        return effectiveLevel
    }

    private fun getPrayerRangedMultiplier(player: Player): Double =
        when {
            Prayers.isActive(player, Prayer.SHARP_EYE) -> 1.05
            Prayers.isActive(player, Prayer.HAWK_EYE) -> 1.10
            Prayers.isActive(player, Prayer.EAGLE_EYE) -> 1.15
            Prayers.isActive(player, Prayer.RIGOUR) -> 1.23
            else -> 1.0
        }

    private fun getPrayerAttackMultiplier(player: Player): Double =
        when {
            Prayers.isActive(player, Prayer.SHARP_EYE) -> 1.05
            Prayers.isActive(player, Prayer.HAWK_EYE) -> 1.10
            Prayers.isActive(player, Prayer.EAGLE_EYE) -> 1.15
            Prayers.isActive(player, Prayer.RIGOUR) -> 1.20
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

    private fun applyPassiveMultiplier(
        player: Player,
        target: Pawn,
        base: Double,
    ): Double {
        when {
            player.hasWeaponType(WeaponType.CROSSBOW) && player.attr.has(Combat.BOLT_ENCHANTMENT_EFFECT) -> {
                val dragonstone =
                    player.hasEquipped(
                        EquipmentType.AMMO,
                        "item.dragonstone_bolts",
                        "item.dragonstone_bolts_e",
                        "item.dragonstone_dragon_bolts",
                        "item.dragonstone_dragon_bolts_e",
                    )
                val opal =
                    player.hasEquipped(
                        EquipmentType.AMMO,
                        "item.opal_bolts",
                        "item.opal_bolts_e",
                        "item.opal_dragon_bolts",
                        "item.opal_dragon_bolts_e",
                    )
                val pearl =
                    player.hasEquipped(
                        EquipmentType.AMMO,
                        "item.pearl_bolts",
                        "item.pearl_bolts_e",
                        "item.pearl_dragon_bolts",
                        "item.pearl_dragon_bolts_e",
                    )

                when {
                    dragonstone -> return base + Math.floor(player.getSkills().getCurrentLevel(Skills.RANGED) / 5.0)
                    opal -> return base + Math.floor(player.getSkills().getCurrentLevel(Skills.RANGED) / 10.0)
                    pearl ->
                        return base +
                            Math.floor(
                                player.getSkills().getCurrentLevel(Skills.RANGED) / (if (isFiery(target)) 15.0 else 20.0),
                            )
                }
            }
        }
        return base
    }

    private fun getDamageDealMultiplier(pawn: Pawn): Double = pawn.attr[Combat.DAMAGE_DEAL_MULTIPLIER] ?: 1.0

    private fun getDamageTakeMultiplier(pawn: Pawn): Double = pawn.attr[Combat.DAMAGE_TAKE_MULTIPLIER] ?: 1.0

    private fun isDragon(pawn: Pawn): Boolean {
        if (pawn.entityType.isNpc) {
            return (pawn as Npc).isSpecies(NpcSpecies.DRACONIC)
        }
        return false
    }

    private fun isFiery(pawn: Pawn): Boolean {
        if (pawn.entityType.isNpc) {
            return (pawn as Npc).isSpecies(NpcSpecies.FIERY)
        }
        return false
    }

    private fun isWearingTorag(player: Player): Boolean {
        return player.hasEquipped(
            EquipmentType.HEAD,
            "item.torags_helm",
            "item.torags_helm_25",
            "item.torags_helm_50",
            "item.torags_helm_75",
            "item.torags_helm_100",
        ) &&
            player.hasEquipped(
                EquipmentType.WEAPON,
                "item.torags_hammers",
                "item.torags_hammers_25",
                "item.torags_hammers_50",
                "item.torags_hammers_75",
                "item.torags_hammers_100",
            ) &&
            player.hasEquipped(
                EquipmentType.CHEST,
                "item.torags_platebody",
                "item.torags_platebody_25",
                "item.torags_platebody_50",
                "item.torags_platebody_75",
                "item.torags_platebody_100",
            ) &&
            player.hasEquipped(
                EquipmentType.LEGS,
                "item.torags_platelegs",
                "item.torags_platelegs_25",
                "item.torags_platelegs_50",
                "item.torags_platelegs_75",
                "item.torags_platelegs_100",
            )
    }
}
