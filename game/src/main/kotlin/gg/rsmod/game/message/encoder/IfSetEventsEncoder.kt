package gg.rsmod.game.message.encoder

import gg.rsmod.game.message.MessageEncoder
import gg.rsmod.game.message.impl.IfSetEventsMessage

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IfSetEventsEncoder : MessageEncoder<IfSetEventsMessage>() {

    override fun extract(message: IfSetEventsMessage, key: String): Number = when (key) {
        "hash" -> message.hash.also {
            println("IfSetEVENT:HASH: ${message.hash}")
        }
        "from" -> message.fromChild.also {
            println("IfSetEVENT:fromChild: ${message.fromChild}")
        }
        "to" -> message.toChild.also {
            println("IfSetEVENT:TO: ${message.toChild}")
        }
        "setting" -> message.setting.also {
            println("IfSetEVENT:SETTING: ${message.setting}")
        }
        else -> throw Exception("Unhandled value key.")
    }

    override fun extractBytes(message: IfSetEventsMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}