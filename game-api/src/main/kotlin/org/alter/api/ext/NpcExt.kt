package org.alter.api.ext

import org.alter.api.NpcSpecies
import org.alter.game.model.Tile
import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.CombatClass
import org.alter.game.model.combat.CombatStyle
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Projectile

const val NPC_ATTACK_BONUS_INDEX = 10
const val NPC_STRENGTH_BONUS_INDEX = 11
const val NPC_RANGED_STRENGTH_BONUS_INDEX = 12
const val NPC_MAGIC_DAMAGE_BONUS_INDEX = 13

fun Npc.prepareAttack(
    combatClass: CombatClass,
    combatStyle: CombatStyle,
    attackStyle: AttackStyle,
) {
    this.combatClass = combatClass
    this.combatStyle = combatStyle
    this.attackStyle = attackStyle
}

fun Npc.createProjectile(
    target: Pawn,
    gfx: Int,
    startHeight: Int,
    endHeight: Int,
    delay: Int,
    angle: Int,
    lifespan: Int = -1,
    steepness: Int = -1,
): Projectile {
    val start = getFrontFacingTile(target)
    val builder =
        Projectile.Builder()
            .setTiles(start = start, target = target)
            .setGfx(gfx = gfx)
            .setHeights(startHeight = startHeight, endHeight = endHeight)
            .setSlope(angle = angle, steepness = if (steepness == -1) Math.min(255, ((getSize() shr 1) + 1) * 32) else steepness)
            .setTimes(
                delay = delay,
                lifespan = if (lifespan == -1) (delay + (start.getChebyshevDistance(target.getCentreTile()) * 5)) else lifespan,
            )

    return builder.build()
}

fun Npc.createProjectile(
    target: Tile,
    gfx: Int,
    startHeight: Int,
    endHeight: Int,
    delay: Int,
    angle: Int,
    lifespan: Int,
): Projectile {
    val builder =
        Projectile.Builder()
            .setTiles(start = getFrontFacingTile(target), target = target)
            .setGfx(gfx = gfx)
            .setHeights(startHeight = startHeight, endHeight = endHeight)
            .setSlope(angle = angle, steepness = Math.min(255, ((getSize() shr 1) + 1) * 32))
            .setTimes(delay = delay, lifespan = lifespan)

    return builder.build()
}

/**
 * Check if npc belongs to any of the species specified.
 *
 * @return true if [Npc.species] contains [species] or any value in [others].
 */
fun Npc.isSpecies(
    species: NpcSpecies,
    vararg others: NpcSpecies,
): Boolean = this.species.contains(species) || this.species.any { others.contains(it) }

fun Npc.getAttackBonus(): Int = equipmentBonuses[NPC_ATTACK_BONUS_INDEX]

fun Npc.getStrengthBonus(): Int = equipmentBonuses[NPC_STRENGTH_BONUS_INDEX]

fun Npc.getRangedStrengthBonus(): Int = equipmentBonuses[NPC_RANGED_STRENGTH_BONUS_INDEX]

fun Npc.getMagicDamageBonus(): Int = equipmentBonuses[NPC_MAGIC_DAMAGE_BONUS_INDEX]
