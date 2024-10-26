package org.alter.plugins.content.combat

import org.alter.game.model.attr.COMBAT_TARGET_FOCUS_ATTR
import org.alter.game.model.attr.FACING_PAWN_ATTR
import org.alter.game.model.attr.INTERACTING_PLAYER_ATTR
import org.alter.game.model.move.*
import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.combat.specialattack.SpecialAttacks
import org.alter.plugins.content.combat.strategy.magic.CombatSpell
import org.alter.plugins.content.interfaces.attack.AttackTab
import org.rsmod.game.pathfinder.PathFinder

on_command("aboutnpc", Privilege.DEV_POWER, description = "Npc information") {
    world.npcs.entries.filter { it?.id == Npcs.IMP_3134 }.forEach { imp ->
        if (imp != null) {
            with (imp) {
                val route = world.pathFinder.findPath(
                    level = tile.height,
                    srcX = tile.x,
                    srcZ = tile.z,
                    destZ = tile.z + 4,
                    destX = tile.x
                )
                imp.walkPath(route, MovementQueue.StepType.NORMAL)
                imp.queue {
                    while(imp.hasMoveDestination()) {
                        imp.forceChat("Moving: ${imp.tile}")
                        wait(1)
                    }
                }
            }
        }
    }
}
/**
 * @TODO Turns out npc's don't walk. But it assigns it's tile.
 */
set_combat_logic {
    pawn.attr[COMBAT_TARGET_FOCUS_ATTR]?.get()?.let { target ->
        pawn.facePawn(target)
    }
    if (pawn.entityType.isPlayer) {
        pawn.queue {
            while (true) {
                if (!cycle(this)) {
                    break
                }
                wait(1)
            }
        }
    }

}

on_player_option("Attack") {
    val target = pawn.attr[INTERACTING_PLAYER_ATTR]?.get() ?: return@on_player_option
    player.attack(target)
}

/**
 * @TODO Problem:
 * 1) Can attack outside range when blocked by obstacles.
 * 2) Noticed GE has 2 levels ?
 *
 *
 *
 * Think current problem is due to NPC Avatar -> It shadow moves the npc.
 */
suspend fun cycle(it: QueueTask): Boolean {
    val pawn = it.pawn
    val target = pawn.getCombatTarget() ?: return false
    val strategy = CombatConfigs.getCombatStrategy(pawn)
    val attackRange = strategy.getAttackRange(pawn)
    if (target != pawn.attr[FACING_PAWN_ATTR]?.get()) { return false }
    /**
     * @TODO Add logic if under same tile
     */
    if (!pawn.tile.isWithinRadius(target.tile, attackRange)) {
        val otherTile: () -> List<Tile> = {
            /**
             * @TODO Add size support
             * @TODO Auto retaliate fucks up [attackRange]
             * @TODO Convert this to a method as we can reuse it trough out other actions
             */
            val tileMap = mutableListOf<Tile>()
            for (dx in -1..1) {
                if (dx != 0) {
                    tileMap.add(Tile(target.tile.x + dx, target.tile.z))
                }
            }
            for (dz in -1..1) {
                if (dz != 0) {
                    tileMap.add(Tile(target.tile.x, target.tile.z + dz))
                }
            }
            tileMap
        }
        val tTile = otherTile().minBy { pawn.tile.getDistance(it) }
        val routeFinding = PathFinder(pawn.world.collision)
        val route = routeFinding.findPath(
            level = pawn.tile.height,
            srcX = pawn.tile.x,
            srcZ = pawn.tile.z,
            destX = tTile.x,
            destZ = tTile.z,
            destWidth = 0,
            destHeight = 0
        )
        pawn.pathGoal = Pawn.Interaction(target.entityType, attackRange, target.tile)
        pawn.walkPath(route, MovementQueue.StepType.NORMAL)
        pawn.facePawn(target)

        if (pawn.entityType.isPlayer) {
            println("Route: $route")
        }
        while (pawn.hasMoveDestination() || !pawn.tile.isWithinRadius(target.tile, attackRange)) {
            if (pawn.entityType.isPlayer) {
                (pawn as Player).message("Holding")
            }
            it.wait(1)
        }
    } else {
        pawn.stopMovement()
    }

    if (pawn.entityType.isPlayer) {
        (pawn as Player).message("Distance: ${pawn.tile.getDistance(target.tile)} AttackRange: $attackRange")
        println("Pawn tile: ${pawn.tile} | Target tile: ${target.tile}")

    }

    /**if (!Combat.canEngage(pawn, target)) {
    Combat.reset(pawn)
    pawn.resetFacePawn()
    return false
    }
    if (!pawn.lock.canAttack()) {
    Combat.reset(pawn)
    return false
    }*/
    if (pawn.entityType.isPlayer) {
        (pawn as Player).message("Executing")
    }


    // Attack Logic
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
    //if (pawn is Player && pawn.getEquipment(EquipmentType.WEAPON) != null && world.plugins.executeWeaponCombatLogic(pawn, pawn.getEquipment(EquipmentType.WEAPON)!!.id)) else
}


/**
 * @TODO Block test
 */
spawn_obj(41728, 3181, 3474)
spawn_obj(41728, 3180, 3473)
