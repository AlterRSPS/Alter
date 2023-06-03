package org.alter.game.model.region.update

import org.alter.game.message.Message
import org.alter.game.message.impl.SoundAreaMessage
import org.alter.game.model.entity.AreaSound

/**
 * Represents an update where a [AreaSound] is spawned.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class SoundAreaUpdate(override val type: EntityUpdateType,
                      override val entity: AreaSound) : EntityUpdate<AreaSound>(type, entity) {

    override fun toMessage(): Message = SoundAreaMessage(((entity.tile.x and 0x7) shl 4) or (entity.tile.z and 0x7), entity.id,
            entity.radius, entity.volume, entity.delay)
}