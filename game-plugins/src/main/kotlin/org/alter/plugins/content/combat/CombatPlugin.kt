package org.alter.plugins.content.combat

import org.alter.api.EquipmentType
import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.Direction
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.attr.COMBAT_TARGET_FOCUS_ATTR
import org.alter.game.model.attr.FACING_PAWN_ATTR
import org.alter.game.model.attr.INTERACTING_PLAYER_ATTR
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.game.model.move.MovementQueue.StepType
import org.alter.game.model.move.hasMoveDestination
import org.alter.game.model.move.stopMovement
import org.alter.game.model.move.walkRoute
import org.alter.game.model.queue.QueueTask
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import org.alter.plugins.content.combat.specialattack.SpecialAttacks
import org.alter.plugins.content.combat.strategy.magic.CombatSpell
import org.alter.plugins.content.interfaces.attack.AttackTab
import java.util.*

class CombatPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {

    init {
        setCombatLogic {
            pawn.attr[COMBAT_TARGET_FOCUS_ATTR]?.get()?.let { target ->
                pawn.facePawn(target)
            }
            pawn.queue {
                while (true) {
                    // @TODO Npc can follow player up to 16 tiles from spawn point, some npc will have exceptional range so property for overwrite should be added.
                    if (!cycle(pawn, this)) {
                        break
                    }
                    wait(1)
                }
            }
        }

        onPlayerOption("Attack") {
            val target = pawn.attr[INTERACTING_PLAYER_ATTR]?.get() ?: return@onPlayerOption
            player.attack(target)
        }
    }

    /**
     * @TODO Bigger creatures seem to have bugged range + their route finding sucks due to conditions given.
     */
    suspend fun cycle(pawn: Pawn, queue: QueueTask): Boolean {
        val target = pawn.getCombatTarget() ?: return false
        val strategy = CombatConfigs.getCombatStrategy(pawn)
        val attackRange = strategy.getAttackRange(pawn)
        var routeLogic = 1
        if (target != pawn.attr[FACING_PAWN_ATTR]?.get()) {
            return false
        }
        if (pawn.entityType.isNpc) {
            routeLogic = (pawn as Npc).routeLogic
        }
        var reached = world.reachStrategy.reached(
            flags = world.collision,
            level = pawn.tile.height,
            srcX = pawn.tile.x ,
            srcZ = pawn.tile.z,
            destX = target.tile.x,
            destZ = target.tile.z,
            destWidth = target.getSize(),
            destLength = target.getSize(),
            srcSize = pawn.getSize(),
            locShape = -2
        )
        if (!reached) {
            when (routeLogic) {
                1 -> {
                    val route = world.smartRouteFinder.findRoute(
                        level = pawn.tile.height,
                        srcX = pawn.tile.x,
                        srcZ = pawn.tile.z,
                        destX = target.tile.x,
                        destZ = target.tile.z,
                        locShape = -2,
                        destWidth = target.getSize(),
                        destLength = target.getSize()
                    )
                    pawn.walkRoute(route, StepType.NORMAL)
                }
                0 -> {
                    val route = LinkedList<Tile>()
                    val destination = world.dumbRouteFinder.naiveDestination(
                        sourceX = pawn.tile.x,
                        sourceZ = pawn.tile.z,
                        sourceWidth = pawn.getSize(),
                        sourceLength = pawn.getSize(),
                        targetX = target.tile.x,
                        targetZ = target.tile.z,
                        targetWidth = target.getSize(),
                        targetLength = target.getSize()
                    )
                    val dx = destination.x - pawn.tile.x
                    val dz = destination.z - pawn.tile.z
                    // Try diagonal move (both x and z)
                    val diagonalMove = Tile(pawn.tile.x + dx.coerceIn(-1, 1), pawn.tile.z + dz.coerceIn(-1, 1))
                    if (!world.canTraverse(pawn.tile, Direction.between(pawn.tile, diagonalMove), pawn, pawn.getSize())) {
                        // If diagonal blocked, try horizontal (east/west)
                        val horizontalMove = Tile(pawn.tile.x + dx.coerceIn(-1, 1), pawn.tile.z)
                        if (!world.canTraverse(pawn.tile, Direction.between(pawn.tile, horizontalMove), pawn, pawn.getSize())) {
                            // If horizontal blocked, try vertical (north/south)
                            val verticalMove = Tile(pawn.tile.x, pawn.tile.z + dz.coerceIn(-1, 1))
                            if (world.canTraverse(pawn.tile, Direction.between(pawn.tile, verticalMove), pawn, pawn.getSize())) {
                                route.add(verticalMove)
                            }
                        } else {
                            route.add(horizontalMove)
                        }
                    } else {
                        route.add(diagonalMove)
                    }
                    if (route.isEmpty()) {
                        pawn.forceChat("Broke")
                        return true
                    }
                    pawn.walkRoute(route, stepType = StepType.NORMAL)
                }
            }
        }
        if (pawn.tile.getDistance(target.tile) <= attackRange + target.getSize()) {
            reached = true
            pawn.stopMovement()
        }
        while (pawn.hasMoveDestination() || !reached) {
            queue.wait(1)
            if (!target.isAlive()) {
                return false
            }
            return cycle(pawn, queue)
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
}