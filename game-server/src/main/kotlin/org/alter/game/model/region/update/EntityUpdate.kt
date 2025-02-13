package org.alter.game.model.region.update

import gg.rsmod.util.toStringHelper
import net.rsprot.protocol.message.ZoneProt
import org.alter.game.model.entity.Entity

/**
 * Represents an update for an [Entity], in which they can be spawned
 * or removed from a client's viewport.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class EntityUpdate<T : Entity>(
    open val entity: T
) {
    abstract fun toMessage(): ZoneProt

    override fun toString(): String = toStringHelper().add("entity", entity).toString()
}
