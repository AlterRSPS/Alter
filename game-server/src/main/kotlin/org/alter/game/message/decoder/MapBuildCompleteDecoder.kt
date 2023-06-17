package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.MapBuildCompleteMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MapBuildCompleteDecoder : MessageDecoder<MapBuildCompleteMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): MapBuildCompleteMessage = MapBuildCompleteMessage()
}