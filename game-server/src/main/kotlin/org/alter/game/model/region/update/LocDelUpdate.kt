package org.alter.game.model.region.update

import net.rsprot.protocol.game.outgoing.zone.payload.LocDel
import net.rsprot.protocol.message.ZoneProt
import org.alter.game.model.entity.GameObject

/**
 * Represents an update where a [GameObject] is removed.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class LocDelUpdate(
    override val entity: GameObject,
) : EntityUpdate<GameObject>(entity) {
    override fun toMessage(): ZoneProt = LocDel((entity.tile.x and 0x7), (entity.tile.z and 0x7), entity.type, entity.rot)
}
