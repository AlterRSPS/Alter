package org.alter.game.model.entity

import org.alter.game.model.EntityType
import org.alter.game.model.Tile

/**
 * A [DynamicObject] is a game object that can be spawned by the [org.alter.game.model.World].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class DynamicObject(id: Int, type: Int, rot: Int, tile: Tile) : GameObject(id, type, rot, tile) {
    constructor(other: GameObject) : this(other.id, other.type, other.rot, other.tile)

    constructor(other: GameObject, id: Int) : this(id, other.type, other.rot, other.tile)

    override val entityType: EntityType = EntityType.DYNAMIC_OBJECT
}
