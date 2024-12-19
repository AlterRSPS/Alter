package org.alter.game.model

import org.alter.game.model.entity.GameObject
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Player

/**
 * @TODO Move out if this end up being good decision :kekw:
 * Interaction goal passed within Pawn class ??
 * When reached null on new path assign null (?)
 * @param [type] - Targets type [Npc] [Player] [GameObject]
 * @param [range] - Tile range before stop
 * @param [goal] - Tile of target
 */
data class Interaction(val type: EntityType, val range: Int, val goal: Tile)
