package org.alter.game.model.region.update

import net.rsprot.protocol.game.outgoing.util.OpFlags
import net.rsprot.protocol.game.outgoing.zone.payload.ObjAdd
import net.rsprot.protocol.message.ZoneProt
import org.alter.game.model.entity.GroundItem

/**
 * Represents an update where a [GroundItem] is spawned.
 *
 * Items placed on a table appears to others after 60s, if they were in the same world as when you placed the items.
 * Items will despawn after 10 minutes.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ObjAddUpdate(
    override val entity: GroundItem,
) : EntityUpdate<GroundItem>(entity) {
    override fun toMessage(): ZoneProt =
        ObjAdd(
            id = entity.item,
            quantity = entity.amount,
            (entity.tile.x and 0x7),
            (entity.tile.z and 0x7),
            opFlags = OpFlags.ALL_SHOWN,
            timeUntilPublic = entity.timeUntilPublic,
            timeUntilDespawn = entity.timeUntilDespawn,
            ownershipType = entity.ownerShipType,
            neverBecomesPublic = true
        )
}
