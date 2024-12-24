package org.alter.plugins.content.combat

import org.alter.game.model.attr.COMBAT_TARGET_FOCUS_ATTR
import org.alter.game.model.attr.FACING_PAWN_ATTR
import org.alter.game.model.attr.INTERACTING_PLAYER_ATTR
import org.alter.game.model.move.*
import org.alter.game.model.move.MovementQueue.StepType
import org.alter.plugins.content.combat.specialattack.SpecialAttacks
import org.alter.plugins.content.combat.strategy.magic.CombatSpell
import org.alter.plugins.content.interfaces.attack.AttackTab

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
 * @TODO Add ranged destination. As right now it will always walk next to target, and then clean up and polish this spaghetti mess.
 */
suspend fun cycle(queue: QueueTask): Boolean {
    val pawn = queue.pawn
    val target = pawn.getCombatTarget() ?: return false
    val strategy = CombatConfigs.getCombatStrategy(pawn)
    val attackRange = strategy.getAttackRange(pawn)
    var pathLogic = 1

    if (target != pawn.attr[FACING_PAWN_ATTR]?.get()) {
        return false
    }

    /**
     * Players always use Smart pathfinder,
     * Npc most of them use Dumb pathfinder and some use smart pathfinder.
     */
    if (pawn.entityType.isNpc) {
        pathLogic = (pawn as Npc).pathLogic
    }
    val reached = world.reachStrategy.reached(
        flags = world.collision,
        level = pawn.tile.height,
        srcX = pawn.tile.x,
        srcZ = pawn.tile.z,
        destX = target.tile.x,
        destZ = target.tile.z,
        destWidth = target.getSize(),
        destHeight = target.getSize(),
        srcSize = pawn.getSize(),
        objShape = -2
    )
    if (!reached) {
        when (pathLogic) {
            1 -> {
                println("p1")

                val route = world.smartPathFinder.findPath(
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

            }

            0 -> {
                println("p0")
                val destination = world.dumbPathFinder.naiveDestination(
                    sourceX = pawn.tile.x,
                    sourceZ = pawn.tile.z,
                    sourceWidth = pawn.getSize(),
                    sourceHeight = pawn.getSize(),
                    targetX = target.tile.x,
                    targetZ = target.tile.z,
                    targetWidth = target.getSize(),
                    targetHeight = target.getSize()
                )
                val direction = Direction.between(pawn.tile, Tile(destination.x, destination.z))
                /**
                 * Ran behind a pillar and it caused inf loop freezing entire server
                 */
                if (!pawn.world.canTraverse(
                        source = pawn.tile,
                        direction = direction,
                        pawn = pawn,
                        srcSize = pawn.getSize()
                    )
                ) {
                    val targetsTile = target.tile
                    while (!pawn.tile.isWithinRadius(
                            targetsTile,
                            target.getSize() + attackRange
                        ) && !pawn.hasMoveDestination()
                    ) {
                        listOfNotNull(
                            Tile(1, 1),
                            Tile(1, 0),
                            Tile(0, 1)
                        ).forEachIndexed { index, it ->
                            println("$index : ${pawn.tile.transform(it.x, it.z)}")

                            /**
                             * @TODO This shit uses smart pathing btw
                             */
                            pawn.walkTo(Tile(pawn.tile.transform(it.x, it.z)))
                            if (!world.canTraverse(
                                    source = pawn.tile,
                                    direction = Direction.between(
                                        pawn.tile,
                                        Tile(destination.x, destination.z).transform(it.x, it.z)
                                    ),
                                    pawn = pawn,
                                    srcSize = pawn.getSize()
                                )
                            ) {
                                queue.wait(1)
                            } else {
                                if (pawn.entityType.isNpc) {
                                    return true
                                }

                            }
                        }
                        /**
                         * We return true for the Npc to keep targeting the player -> Need to test with 2 Pawn's if the npc will change target or no.
                         * @TODO Same need to check how this logic would happen on OSRS
                         */

                    }


                } else {
                    pawn.walkPath(destination)
                }


            }
        }
    }
    while (pawn.hasMoveDestination() || !reached) {
        queue.wait(1)
        if (!target.isAlive()) {
            return false
        }
        return cycle(queue)
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
