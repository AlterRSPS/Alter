package org.alter.plugins.content.combat

import org.alter.game.model.attr.COMBAT_TARGET_FOCUS_ATTR
import org.alter.game.model.attr.FACING_PAWN_ATTR
import org.alter.game.model.attr.INTERACTING_PLAYER_ATTR
import org.alter.game.model.move.*
import org.alter.game.model.move.MovementQueue.StepType
import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.combat.specialattack.SpecialAttacks
import org.alter.plugins.content.combat.strategy.magic.CombatSpell
import org.alter.plugins.content.interfaces.attack.AttackTab

on_command("aboutnpc", Privilege.DEV_POWER, description = "Npc information") {
    world.npcs.entries.filter { it?.id == Npcs.IMP_3134 }.forEach { imp ->
        if (imp != null) {
            with(imp) {
                val route = world.pathFinder.findPath(
                    level = tile.height,
                    srcX = tile.x,
                    srcZ = tile.z,
                    destZ = tile.z + 4,
                    destX = tile.x
                )
                imp.walkPath(route, MovementQueue.StepType.NORMAL)
                imp.queue {
                    while (imp.hasMoveDestination()) {
                        imp.forceChat("Moving: ${imp.tile}")
                        wait(1)
                    }
                }
            }
        }
    }
}


set_combat_logic {
    pawn.attr[COMBAT_TARGET_FOCUS_ATTR]?.get()?.let { target ->
        pawn.facePawn(target)
    }
    pawn.queue {
        while (true) {
            if (!cycle(this)) {
                break
            }
            wait(1)
        }
    }
}

on_player_option("Attack") {
    val target = pawn.attr[INTERACTING_PLAYER_ATTR]?.get() ?: return@on_player_option
    player.attack(target)
}

/**
 * 1) Can attack outside range when blocked by obstacles.
 */
suspend fun cycle(it: QueueTask): Boolean {
    val pawn = it.pawn
    val target = pawn.getCombatTarget() ?: return false
    val strategy = CombatConfigs.getCombatStrategy(pawn)
    val attackRange = strategy.getAttackRange(pawn)
    if (target != pawn.attr[FACING_PAWN_ATTR]?.get()) {
        return false
    }
    val route = world.pathFinder.findPath(
        level = pawn.tile.height,
        srcX = pawn.tile.x,
        srcZ = pawn.tile.z,
        destX = target.tile.x,
        destZ = target.tile.z,
        objShape = -2,
        destWidth = target.getSize(),
        destHeight = target.getSize()
    )
    pawn.walkPath(route, StepType.NORMAL)
    while (pawn.hasMoveDestination() || !pawn.tile.isWithinRadius(target.tile, attackRange)) {
        it.wait(1)
        if (!target.isAlive()) {
            return false
        }
    }
    if (!Combat.canEngage(pawn, target)) {
        Combat.reset(pawn)
        pawn.resetFacePawn()
        return false
    }
    if (!pawn.lock.canAttack()) {
        Combat.reset(pawn)
        return false
    }
    if (pawn is Player) {
        pawn.setVarp(Combat.PRIORITY_PID_VARP, target.index)
        if (!pawn.attr.has(Combat.CASTING_SPELL) && pawn.getVarbit(Combat.SELECTED_AUTOCAST_VARBIT) != 0) {
            val spell =
                CombatSpell.values.firstOrNull { it.autoCastId == pawn.getVarbit(Combat.SELECTED_AUTOCAST_VARBIT) }
            if (spell != null) {
                pawn.attr[Combat.CASTING_SPELL] = spell
            }
        }
    }
    if (target != pawn.attr[FACING_PAWN_ATTR]?.get()) {
        return false
    }
    if (Combat.isAttackDelayReady(pawn)) {
        if (Combat.canAttack(pawn, target, strategy)) {
            if (pawn is Player && AttackTab.isSpecialEnabled(pawn) && pawn.getEquipment(EquipmentType.WEAPON) != null) {
                AttackTab.disableSpecial(pawn)
                if (SpecialAttacks.execute(pawn, target, world)) {
                    Combat.postAttack(pawn, target)
                    return true
                }
                pawn.message("You don't have enough power left.")
            }
            strategy.attack(pawn, target)
            Combat.postAttack(pawn, target)
        } else {
            Combat.reset(pawn)
            return false
        }
    }
    return true
}


/**
 * @TODO Block test
 */
spawn_obj(41728, 3181, 3474)
spawn_obj(41728, 3180, 3473)
