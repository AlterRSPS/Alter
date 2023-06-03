package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.ResumePNameDialogMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ResumePNameDialogDecoder : MessageDecoder<ResumePNameDialogMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): ResumePNameDialogMessage {
        val name = stringValues["name"]!!
        return ResumePNameDialogMessage(name)
    }
}