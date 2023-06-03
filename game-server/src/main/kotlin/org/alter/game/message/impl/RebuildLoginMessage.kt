package org.alter.game.message.impl

import org.alter.game.message.Message
import org.alter.game.model.Tile
import org.alter.game.service.xtea.XteaKeyService

/**
 * @author Tom <rspsmods@gmail.com>
 */
class RebuildLoginMessage(val playerIndex: Int, val tile: Tile, val playerTiles: IntArray, val xteaKeyService: XteaKeyService?) : Message