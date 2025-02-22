package org.alter.plugins.content.npcs.kbd

import org.alter.api.*
import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.CombatClass
import org.alter.game.model.combat.CombatStyle
import org.alter.game.model.entity.*
import org.alter.game.model.queue.*
import org.alter.game.plugin.*
import org.alter.plugins.content.combat.*
import org.alter.plugins.content.combat.formula.DragonfireFormula
import org.alter.plugins.content.combat.formula.MeleeCombatFormula
import org.alter.plugins.content.combat.strategy.RangedCombatStrategy
import org.alter.plugins.content.mechanics.poison.poison

class KbdCombatPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        onNpcCombat("npc.king_black_dragon") {
            npc.queue {
                npc.combat(this)
            }
        }
    }

    private suspend fun Npc.combat(it: QueueTask) {
        var target = getCombatTarget() ?: return

        while (canEngageCombat(target)) {
            facePawn(target)
            if (moveToAttackRange(it, target, distance = 6, projectile = true) && isAttackDelayReady()) {
                if (this.world.chance(1, 4) && canAttackMelee(it, target, moveIfNeeded = false)) {
                    this.meleeAttack(target)
                } else {
                    when (this.world.random(3)) {
                        0 -> fireAttack(this, target)
                        1 -> this.poisonAttack(target)
                        2 -> this.freezeAttack(target)
                        3 -> this.shockAttack(target)
                    }
                }
                postAttackLogic(target)
            }
            it.wait(1)
            target = getCombatTarget() ?: break
        }

        resetFacePawn()
        removeCombatTarget()
    }

    private fun Npc.meleeAttack(
        target: Pawn,
    ) {
        if (this.world.chance(1, 2)) {
            // Headbutt attack
            prepareAttack(CombatClass.MELEE, CombatStyle.STAB, AttackStyle.ACCURATE)
            animate(91)
        } else {
            // Claw attack
            prepareAttack(CombatClass.MELEE, CombatStyle.SLASH, AttackStyle.AGGRESSIVE)
            animate(80)
        }
        if (MeleeCombatFormula.getAccuracy(this, target) >= this.world.randomDouble()) {
            target.hit(this.world.random(26), type = HitType.HIT, delay = 1)
        } else {
            target.hit(damage = 0, type = HitType.BLOCK, delay = 1)
        }
    }

    private fun fireAttack(
        npc: Npc,
        target: Pawn,
    ) {
        val projectile = npc.createProjectile(target, gfx = 393, startHeight = 43, endHeight = 31, delay = 51, angle = 15, steepness = 127)
        npc.prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
        npc.animate(81)
        world.spawn(projectile)
        npc.dealHit(
            target = target,
            formula = DragonfireFormula(maxHit = 65),
            delay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1,
        )
    }

    private fun Npc.poisonAttack(
        target: Pawn,
    ) {
        val projectile = createProjectile(target, gfx = 394, startHeight = 43, endHeight = 31, delay = 51, angle = 15, steepness = 127)
        prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
        animate(82)
        this.world.spawn(projectile)
        val hit =
            dealHit(
                target = target,
                formula = DragonfireFormula(maxHit = 65, minHit = 10),
                delay = RangedCombatStrategy.getHitDelay(getFrontFacingTile(target), target.getCentreTile()) - 1,
            ) {
                if (it.landed() && this.world.chance(1, 6)) {
                    target.poison(initialDamage = 8) {
                        if (target is Player) {
                            target.message("You have been poisoned.")
                        }
                    }
                }
            }
        if (hit.blocked()) {
            target.graphic(id = 85, height = 124, delay = hit.getClientHitDelay())
        }
    }

    private fun Npc.freezeAttack(
        target: Pawn,
    ) {
        val projectile = createProjectile(target, gfx = 395, startHeight = 43, endHeight = 31, delay = 51, angle = 15, steepness = 127)
        prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
        animate(83)
        this.world.spawn(projectile)
        val hit =
            dealHit(
                target = target,
                formula = DragonfireFormula(maxHit = 65, minHit = 10),
                delay = RangedCombatStrategy.getHitDelay(getFrontFacingTile(target), target.getCentreTile()) - 1,
            ) {
                if (it.landed() && this.world.chance(1, 6)) {
                    target.freeze(cycles = 6) {
                        if (target is Player) {
                            target.message("You have been frozen.")
                        }
                    }
                }
            }
        if (hit.blocked()) {
            target.graphic(id = 85, height = 124, delay = hit.getClientHitDelay())
        }
    }

    private fun Npc.shockAttack(
        target: Pawn,
    ) {
        val projectile = createProjectile(target, gfx = 396, startHeight = 43, endHeight = 31, delay = 51, angle = 15, steepness = 127)
        prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
        animate(84)
        this.world.spawn(projectile)
        val hit =
            dealHit(
                target = target,
                formula = DragonfireFormula(maxHit = 65, minHit = 12),
                delay = RangedCombatStrategy.getHitDelay(getFrontFacingTile(target), target.getCentreTile()) - 1,
            ) {
                if (it.landed() && this.world.chance(1, 6)) {
                    if (target is Player) {
                        arrayOf(Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.MAGIC, Skills.RANGED).forEach { skill ->
                            target.getSkills().alterCurrentLevel(skill, -2)
                        }
                        target.message("You're shocked and weakened!")
                    }
                }
            }
        if (hit.blocked()) {
            target.graphic(id = 85, height = 124, delay = hit.getClientHitDelay())
        }
    }

}
