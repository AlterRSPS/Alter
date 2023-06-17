package org.alter.game.model.region.update

import org.alter.game.message.Message
import org.alter.game.message.impl.LocDelMessage
import org.alter.game.model.entity.GameObject

/**
 * Represents an update where a [GameObject] is removed.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class LocDelUpdate(override val type: EntityUpdateType,
                   override val entity: GameObject) : EntityUpdate<GameObject>(type, entity) {

    override fun toMessage(): Message = LocDelMessage(entity.settings.toInt(),
            ((entity.tile.x and 0x7) shl 4) or (entity.tile.z and 0x7))
}