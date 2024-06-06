package org.alter.game.model.region.update

import net.rsprot.protocol.game.outgoing.util.OpFlags
import net.rsprot.protocol.game.outgoing.zone.payload.ObjAdd
import net.rsprot.protocol.message.ZoneProt
import org.alter.game.model.entity.GroundItem

/**
 * Represents an update where a [GroundItem] is spawned.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ObjAddUpdate(
    override val type: EntityUpdateType,
    override val entity: GroundItem,
) : EntityUpdate<GroundItem>(type, entity) {
    override fun toMessage(): ZoneProt =
        ObjAdd(
            entity.item,
            entity.amount,
            (entity.tile.x and 0x7),
            (entity.tile.z and 0x7),
            OpFlags.ALL_SHOWN,
        )
}
