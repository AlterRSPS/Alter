package org.alter.game.model.region.update

import net.rsprot.protocol.message.ZoneProt

/**
 * Represents a group of [EntityUpdate]s.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class EntityGroupMessage(val id: Int, val message: ZoneProt)