package org.alter.game.model.region.update

import net.rsprot.protocol.game.outgoing.zone.payload.MapAnim
import net.rsprot.protocol.message.ZoneProt
import org.alter.game.model.Graphic
import org.alter.game.model.TileGraphic

/**
 * Represents an update where a [Graphic] is spawned on a tile.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class MapAnimUpdate(
    override val type: EntityUpdateType,
    override val entity: TileGraphic,
) : EntityUpdate<TileGraphic>(type, entity) {
    override fun toMessage(): ZoneProt =
        MapAnim(
            entity.id,
            entity.delay,
            entity.height,
            (entity.tile.x and 0x7),
            (entity.tile.y and 0x7),
        )
}
