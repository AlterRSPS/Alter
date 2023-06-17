package org.alter.game.message.impl

import org.alter.game.message.Message
import org.alter.game.message.MessageEncoderSet
import org.alter.game.message.MessageStructureSet
import org.alter.game.model.region.update.EntityGroupMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateZonePartialEnclosedMessage(val x: Int, val z: Int, val encoders: MessageEncoderSet, val structures: MessageStructureSet,
                                       vararg val messages: EntityGroupMessage) : Message