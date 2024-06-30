package org.alter.plugins.content.combat.specialattack

import org.alter.game.model.World
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class CombatContext(val world: World, val player: Player) {
    lateinit var target: Pawn
}
