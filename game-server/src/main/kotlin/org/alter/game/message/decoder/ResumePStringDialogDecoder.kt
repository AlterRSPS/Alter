package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.ResumePStringDialogMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePStringDialogDecoder : MessageDecoder<ResumePStringDialogMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): ResumePStringDialogMessage {
        val input = stringValues["input"]!!
        return ResumePStringDialogMessage(input)
    }
}