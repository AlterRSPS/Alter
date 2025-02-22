package org.alter.game.model.region.update

import net.rsprot.protocol.game.outgoing.zone.payload.ObjCount
import net.rsprot.protocol.message.ZoneProt
import org.alter.game.model.entity.GroundItem

/**
 * Represents an update where a [GroundItem]'s [GroundItem.amount] is changed.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ObjCountUpdate(
    override val entity: GroundItem,
    private val oldAmount: Int,
    private val newAmount: Int,
) : EntityUpdate<GroundItem>(entity) {
    override fun toMessage(): ZoneProt =
        ObjCount(
            entity.item,
            oldAmount,
            newAmount,
            (entity.tile.x and 0x7),
            (entity.tile.z and 0x7),
        )
}
