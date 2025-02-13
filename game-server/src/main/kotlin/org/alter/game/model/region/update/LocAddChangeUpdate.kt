package org.alter.game.model.region.update

import net.rsprot.protocol.game.outgoing.util.OpFlags
import net.rsprot.protocol.game.outgoing.zone.payload.LocAddChange
import net.rsprot.protocol.message.ZoneProt
import org.alter.game.model.entity.GameObject

/**
 * Represents an update where a [GameObject] is spawned.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class LocAddChangeUpdate(
    override val entity: GameObject,
) : EntityUpdate<GameObject>(entity) {
    override fun toMessage(): ZoneProt =
        LocAddChange(
            entity.id,
            (entity.tile.x and 0x7),
            (entity.tile.z and 0x7),
            entity.type,
            entity.rot,
            OpFlags.ALL_SHOWN,
        )
}
