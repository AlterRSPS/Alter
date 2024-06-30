package org.alter.game.model.entity

import org.alter.game.model.EntityType
import org.alter.game.model.Tile

/**
 * A sound that can be spawned in a tile.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class AreaSound private constructor(val id: Int, val radius: Int, val volume: Int, val delay: Int) : Entity() {
    constructor(tile: Tile, id: Int, radius: Int, volume: Int, delay: Int = 0) : this(id, radius, volume, delay) {
        check(radius <= 0xf) { "Radius can not exceed 15 tiles." }
        this.tile = tile
    }

    override val entityType: EntityType = EntityType.AREA_SOUND
}
