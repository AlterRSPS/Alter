package org.alter.game.message.decoder

import org.alter.game.message.MessageDecoder
import org.alter.game.message.impl.OpPlayerTMessage

class OpPlayerTDecoder : MessageDecoder<OpPlayerTMessage>() {

    override fun decode(opcode: Int, opcodeIndex: Int, values: HashMap<String, Number>, stringValues: HashMap<String, String>): OpPlayerTMessage {
        val playerIndex = values["player_index"]!!.toInt()
        val keydown = values["keydown"]!!.toInt() == 1
        val verify = values["verify"]!!.toInt()
        val spellChildIndex = values["spell_child_index"]!!.toInt()
        val componentHash = values["component_hash"]!!.toInt()
        return OpPlayerTMessage(playerIndex, keydown, verify, spellChildIndex, componentHash)
    }
}