package org.alter.game.model.region.update

import org.alter.game.message.Message
import org.alter.game.message.impl.ObjDelMessage
import org.alter.game.model.entity.GroundItem

/**
 * Represents an update where a [GroundItem] is removed.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ObjDelUpdate(override val type: EntityUpdateType,
                   override val entity: GroundItem) : EntityUpdate<GroundItem>(type, entity) {

    override fun toMessage(): Message = ObjDelMessage(entity.item, ((entity.tile.x and 0x7) shl 4) or (entity.tile.z and 0x7), entity.amount)
}