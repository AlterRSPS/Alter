package org.alter.plugins.content.combat.formula

import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.CombatStyle
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.api.*
import org.alter.api.ext.*
import org.alter.plugins.content.combat.Combat
import org.alter.plugins.content.combat.CombatConfigs
import org.alter.plugins.content.mechanics.prayer.Prayer
import org.alter.plugins.content.mechanics.prayer.Prayers

/**
 * @author Tom <rspsmods@gmail.com>
 */
object MeleeCombatFormula : CombatFormula {

    private val BLACK_MASKS = arrayOf("item.black_mask",
            "item.black_mask_1", "item.black_mask_2", "item.black_mask_3", "item.black_mask_4",
            "item.black_mask_5", "item.black_mask_6", "item.black_mask_7", "item.black_mask_8",
            "item.black_mask_9", "item.black_mask_10")

    private val BLACK_MASKS_I = arrayOf("item.black_mask_i",
            "item.black_mask_1_i", "item.black_mask_2_i", "item.black_mask_3_i", "item.black_mask_4_i",
            "item.black_mask_5_i", "item.black_mask_6_i", "item.black_mask_7_i", "item.black_mask_8_i",
            "item.black_mask_9_i", "item.black_mask_10_i")

    private val MELEE_VOID = arrayOf("item.void_melee_helm", "item.void_knight_top", "item.void_knight_robe", "item.void_knight_gloves")

    private val MELEE_ELITE_VOID = arrayOf("item.void_melee_helm", "item.elite_void_top", "item.elite_void_robe", "item.void_knight_gloves")

    override fun getAccuracy(pawn: Pawn, target: Pawn, specialAttackMultiplier: Double): Double {
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

    override fun getMaxHit(pawn: Pawn, target: Pawn, specialAttackMultiplier: Double, specialPassiveMultiplier: Double): Int {
        val a = if (pawn is Player) getEffectiveStrengthLevel(pawn) else if (pawn is Npc) getEffectiveStrengthLevel(pawn) else 0.0
        val b = getEquipmentStrengthBonus(pawn)

        var base = Math.floor(0.5 + a * (b + 64.0) / 640.0).toInt()
        if (pawn is Player) {
            base = applyStrengthSpecials(pawn, target, base, specialAttackMultiplier, specialPassiveMultiplier)
        }
        return base
    }

    private fun getAttackRoll(pawn: Pawn, target: Pawn, specialAttackMultiplier: Double): Int {
        val a = if (pawn is Player) getEffectiveAttackLevel(pawn) else if (pawn is Npc) getEffectiveAttackLevel(pawn) else 0.0
        val b = getEquipmentAttackBonus(pawn)

        var maxRoll = a * (b + 64.0)
        if (pawn is Player) {
            maxRoll = applyAttackSpecials(pawn, target, maxRoll, specialAttackMultiplier)
        }
        return maxRoll.toInt()
    }

    private fun getDefenceRoll(pawn: Pawn, target: Pawn): Int {
        val a = if (pawn is Player) getEffectiveDefenceLevel(pawn) else if (pawn is Npc) getEffectiveDefenceLevel(pawn) else 0.0
        val b = getEquipmentDefenceBonus(pawn, target)

        var maxRoll = a * (b + 64.0)
        maxRoll = applyDefenceSpecials(target, maxRoll)
        return maxRoll.toInt()
    }

    private fun applyStrengthSpecials(player: Player, target: Pawn, base: Int, specialAttackMultiplier: Double, specialPassiveMultiplier: Double): Int {
        var hit = base.toDouble()

        hit *= getEquipmentMultiplier(player)
        hit = Math.floor(hit)

        hit *= specialAttackMultiplier
        hit = Math.floor(hit)

        if (target.hasPrayerIcon(PrayerIcon.PROTECT_FROM_MELEE)) {
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

    private fun applyAttackSpecials(player: Player, target: Pawn, base: Double, specialAttackMultiplier: Double): Double {
        var hit = base

        hit *= getEquipmentMultiplier(player)
        hit = Math.floor(hit)

        hit *= (if (player.hasEquipped(EquipmentType.WEAPON, "item.arclight") && isDemon(target)) 1.7 else specialAttackMultiplier)
        hit = Math.floor(hit)

        return hit
    }

    private fun applyDefenceSpecials(target: Pawn, base: Double): Double {
        var hit = base

        if (target is Player && isWearingTorag(target) && target.hasEquipped(EquipmentType.AMULET, "item.amulet_of_the_damned_full")) {
            val lost = (target.getMaxHp() - target.getCurrentHp()) / 100.0
            val max = target.getMaxHp() / 100.0
            hit *= (1.0 + (lost * max))
            hit = Math.floor(hit)
        }

        return hit
    }

    private fun getEquipmentStrengthBonus(pawn: Pawn): Double = when (pawn) {
        is Player -> pawn.getStrengthBonus().toDouble()
        is Npc -> pawn.getStrengthBonus().toDouble()
        else -> throw IllegalArgumentException("Invalid pawn type. $pawn")
    }

    private fun getEquipmentAttackBonus(pawn: Pawn): Double {
        val combatStyle = CombatConfigs.getCombatStyle(pawn)
        val bonus = when (combatStyle) {
            CombatStyle.STAB -> BonusSlot.ATTACK_STAB
            CombatStyle.SLASH -> BonusSlot.ATTACK_SLASH
            CombatStyle.CRUSH -> BonusSlot.ATTACK_CRUSH
            else -> throw IllegalStateException("Invalid combat style. $combatStyle")
        }
        return pawn.getBonus(bonus).toDouble()
    }

    private fun getEquipmentDefenceBonus(pawn: Pawn, target: Pawn): Double {
        val combatStyle = CombatConfigs.getCombatStyle(pawn)
        val bonus = when (combatStyle) {
            CombatStyle.STAB -> BonusSlot.DEFENCE_STAB
            CombatStyle.SLASH -> BonusSlot.DEFENCE_SLASH
            CombatStyle.CRUSH -> BonusSlot.DEFENCE_CRUSH
            else -> throw IllegalStateException("Invalid combat style. $combatStyle")
        }
        return target.getBonus(bonus).toDouble()
    }

    private fun getEffectiveStrengthLevel(player: Player): Double {
        var effectiveLevel = Math.floor(player.getSkills().getCurrentLevel(Skills.STRENGTH) * getPrayerStrengthMultiplier(player))

        effectiveLevel += when (CombatConfigs.getAttackStyle(player)){
            AttackStyle.AGGRESSIVE -> 3.0
            AttackStyle.CONTROLLED -> 1.0
            else -> 0.0
        }

        effectiveLevel += 8.0

        if (player.hasEquipped(MELEE_VOID) || player.hasEquipped(MELEE_ELITE_VOID)) {
            effectiveLevel *= 1.10
            effectiveLevel = Math.floor(effectiveLevel)
        }

        return Math.floor(effectiveLevel)
    }

    private fun getEffectiveAttackLevel(player: Player): Double {
        var effectiveLevel = Math.floor(player.getSkills().getCurrentLevel(Skills.ATTACK) * getPrayerAttackMultiplier(player))

        effectiveLevel += when (CombatConfigs.getAttackStyle(player)){
            AttackStyle.ACCURATE -> 3.0
            AttackStyle.CONTROLLED -> 1.0
            else -> 0.0
        }

        effectiveLevel += 8.0

        if (player.hasEquipped(MELEE_VOID) || player.hasEquipped(MELEE_ELITE_VOID)) {
            effectiveLevel *= 1.10
            effectiveLevel = Math.floor(effectiveLevel)
        }

        return effectiveLevel
    }

    private fun getEffectiveDefenceLevel(player: Player): Double {
        var effectiveLevel = Math.floor(player.getSkills().getCurrentLevel(Skills.DEFENCE) * getPrayerDefenceMultiplier(player))

        effectiveLevel += when (CombatConfigs.getAttackStyle(player)){
            AttackStyle.DEFENSIVE -> 3.0
            AttackStyle.CONTROLLED -> 1.0
            AttackStyle.LONG_RANGE -> 3.0
            else -> 0.0
        }

        effectiveLevel += 8.0

        return Math.floor(effectiveLevel)
    }

    private fun getEffectiveStrengthLevel(npc: Npc): Double {
        var effectiveLevel = npc.stats.getCurrentLevel(NpcSkills.STRENGTH).toDouble()
        effectiveLevel += 8
        return effectiveLevel
    }

    private fun getEffectiveAttackLevel(npc: Npc): Double {
        var effectiveLevel = npc.stats.getCurrentLevel(NpcSkills.ATTACK).toDouble()
        effectiveLevel += 8
        return effectiveLevel
    }

    private fun getEffectiveDefenceLevel(npc: Npc): Double {
        var effectiveLevel = npc.stats.getCurrentLevel(NpcSkills.DEFENCE).toDouble()
        effectiveLevel += 8
        return effectiveLevel
    }

    private fun getPrayerStrengthMultiplier(player: Player): Double = when {
        Prayers.isActive(player, Prayer.BURST_OF_STRENGTH) -> 1.05
        Prayers.isActive(player, Prayer.SUPERHUMAN_STRENGTH) -> 1.10
        Prayers.isActive(player, Prayer.ULTIMATE_STRENGTH) -> 1.15
        Prayers.isActive(player, Prayer.CHIVALRY) -> 1.18
        Prayers.isActive(player, Prayer.PIETY) -> 1.23
        else -> 1.0
    }

    private fun getPrayerAttackMultiplier(player: Player): Double = when {
        Prayers.isActive(player, Prayer.CLARITY_OF_THOUGHT) -> 1.05
        Prayers.isActive(player, Prayer.IMPROVED_REFLEXES) -> 1.10
        Prayers.isActive(player, Prayer.INCREDIBLE_REFLEXES) -> 1.15
        Prayers.isActive(player, Prayer.CHIVALRY) -> 1.15
        Prayers.isActive(player, Prayer.PIETY) -> 1.20
        else -> 1.0
    }

    private fun getPrayerDefenceMultiplier(player: Player): Double = when {
        Prayers.isActive(player, Prayer.THICK_SKIN) -> 1.05
        Prayers.isActive(player, Prayer.ROCK_SKIN) -> 1.10
        Prayers.isActive(player, Prayer.STEEL_SKIN) -> 1.15
        Prayers.isActive(player, Prayer.CHIVALRY) -> 1.20
        Prayers.isActive(player, Prayer.PIETY) -> 1.25
        Prayers.isActive(player, Prayer.RIGOUR) -> 1.25
        Prayers.isActive(player, Prayer.AUGURY) -> 1.25
        else -> 1.0
    }

    private fun getEquipmentMultiplier(player: Player): Double = when {
        player.hasEquipped(EquipmentType.AMULET, "item.salve_amulet") -> 7.0 / 6.0 // These should only apply if the target is undead..
        player.hasEquipped(EquipmentType.AMULET, "item.salve_amulet_e") -> 1.2 // These should only apply if the target is undead..
        // TODO: this should only apply when target is slayer task?
        player.hasEquipped(EquipmentType.HEAD, *BLACK_MASKS) || player.hasEquipped(EquipmentType.HEAD, *BLACK_MASKS_I) -> 7.0 / 6.0 // This should only apply if you have the target || his category as a Slayer Task
        else -> 1.0
    }

    private fun applyPassiveMultiplier(pawn: Pawn, target: Pawn, base: Double): Double {
        if (pawn is Player) {
            val world = pawn.world
            val multiplier = when {
                pawn.hasEquipped(EquipmentType.AMULET, "item.berserker_necklace") -> 1.2
                isWearingDharok(pawn) -> {
                    val lost = (pawn.getMaxHp() - pawn.getCurrentHp()) / 100.0
                    val max = pawn.getMaxHp() / 100.0
                    1.0 + (lost * max)
                }
                pawn.hasEquipped(EquipmentType.WEAPON, "item.gadderhammer") && isShade(target) -> if (world.chance(1, 20)) 2.0 else 1.25
                pawn.hasEquipped(EquipmentType.WEAPON, "item.keris", "item.kerisp") && (isKalphite(target) || isScarab(target)) -> if (world.chance(1, 51)) 3.0 else (4.0 / 3.0)
                else -> 1.0
            }
            if (multiplier == 1.0 && isWearingVerac(pawn)) {
                return base + 1.0
            }
            return base * multiplier
        }
        return base
    }

    private fun getDamageDealMultiplier(pawn: Pawn): Double = pawn.attr[Combat.DAMAGE_DEAL_MULTIPLIER] ?: 1.0

    private fun getDamageTakeMultiplier(pawn: Pawn): Double = pawn.attr[Combat.DAMAGE_TAKE_MULTIPLIER] ?: 1.0

    private fun isDemon(pawn: Pawn): Boolean {
        if (pawn.entityType.isNpc) {
            return (pawn as Npc).isSpecies(NpcSpecies.DEMON)
        }
        return false
    }

    private fun isShade(pawn: Pawn): Boolean {
        if (pawn.entityType.isNpc) {
            return (pawn as Npc).isSpecies(NpcSpecies.SHADE)
        }
        return false
    }

    private fun isKalphite(pawn: Pawn): Boolean {
        if (pawn.entityType.isNpc) {
            return (pawn as Npc).isSpecies(NpcSpecies.KALPHITE)
        }
        return false
    }

    private fun isScarab(pawn: Pawn): Boolean {
        if (pawn.entityType.isNpc) {
            return (pawn as Npc).isSpecies(NpcSpecies.SCARAB)
        }
        return false
    }

    private fun isWearingDharok(pawn: Pawn): Boolean {
        if (pawn.entityType.isPlayer) {
            val player = pawn as Player
            return player.hasEquipped(EquipmentType.HEAD, "item.dharoks_helm", "item.dharoks_helm_25", "item.dharoks_helm_50", "item.dharoks_helm_75", "item.dharoks_helm_100")
                    && player.hasEquipped(EquipmentType.WEAPON, "item.dharoks_greataxe", "item.dharoks_greataxe_25", "item.dharoks_greataxe_50", "item.dharoks_greataxe_75", "item.dharoks_greataxe_100")
                    && player.hasEquipped(EquipmentType.CHEST, "item.dharoks_platebody", "item.dharoks_platebody_25", "item.dharoks_platebody_50", "item.dharoks_platebody_75", "item.dharoks_platebody_100")
                    && player.hasEquipped(EquipmentType.LEGS, "item.dharoks_platelegs", "item.dharoks_platelegs_25", "item.dharoks_platelegs_50", "item.dharoks_platelegs_75", "item.dharoks_platelegs_100")
        }
        return false
    }

    private fun isWearingVerac(pawn: Pawn): Boolean {
        if (pawn.entityType.isPlayer) {
            val player = pawn as Player
            return player.hasEquipped(EquipmentType.HEAD, "item.veracs_helm", "item.veracs_helm_25", "item.veracs_helm_50", "item.veracs_helm_75", "item.veracs_helm_100")
                    && player.hasEquipped(EquipmentType.WEAPON, "item.veracs_flail", "item.veracs_flail_25", "item.veracs_flail_50", "item.veracs_flail_75", "item.veracs_flail_100")
                    && player.hasEquipped(EquipmentType.CHEST, "item.veracs_brassard", "item.veracs_brassard_25", "item.veracs_brassard_50", "item.veracs_brassard_75", "item.veracs_brassard_100")
                    && player.hasEquipped(EquipmentType.LEGS, "item.veracs_plateskirt", "item.veracs_plateskirt_25", "item.veracs_plateskirt_50", "item.veracs_plateskirt_75", "item.veracs_plateskirt_100")
        }
        return false
    }

    private fun isWearingTorag(player: Player): Boolean {
        return player.hasEquipped(EquipmentType.HEAD, "item.torags_helm", "item.torags_helm_25", "item.torags_helm_50", "item.torags_helm_75", "item.torags_helm_100")
                && player.hasEquipped(EquipmentType.WEAPON, "item.torags_hammers", "item.torags_hammers_25", "item.torags_hammers_50", "item.torags_hammers_75", "item.torags_hammers_100")
                && player.hasEquipped(EquipmentType.CHEST, "item.torags_platebody", "item.torags_platebody_25", "item.torags_platebody_50", "item.torags_platebody_75", "item.torags_platebody_100")
                && player.hasEquipped(EquipmentType.LEGS, "item.torags_platelegs", "item.torags_platelegs_25", "item.torags_platelegs_50", "item.torags_platelegs_75", "item.torags_platelegs_100")
    }
}