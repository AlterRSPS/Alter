package org.alter.game.model.entity

import org.alter.game.model.EntityType
import org.alter.game.model.Tile

/**
 * A [StaticObject] is a game object that is part of the static terrain loaded
 * from the game's resources.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class StaticObject(id: Int, type: Int, rot: Int, tile: Tile) : GameObject(id, type, rot, tile) {
    override val entityType: EntityType = EntityType.STATIC_OBJECT
}
