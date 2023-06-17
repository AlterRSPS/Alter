package org.alter.game.message.impl

import org.alter.game.message.Message
import org.alter.game.service.xtea.XteaKeyService

/**
 * @author Tom <rspsmods@gmail.com>
 */
class RebuildNormalMessage(val x: Int, val z: Int, val xteaKeyService: XteaKeyService?) : Message