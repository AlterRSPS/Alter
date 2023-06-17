package org.alter.game.model.region.update

import org.alter.game.message.Message
import org.alter.game.message.impl.LocAddChangeMessage
import org.alter.game.model.entity.GameObject

/**
 * Represents an update where a [GameObject] is spawned.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class LocAddChangeUpdate(override val type: EntityUpdateType,
                         override val entity: GameObject) : EntityUpdate<GameObject>(type, entity) {

    override fun toMessage(): Message = LocAddChangeMessage(entity.id, entity.settings.toInt(),
            ((entity.tile.x and 0x7) shl 4) or (entity.tile.z and 0x7))
}