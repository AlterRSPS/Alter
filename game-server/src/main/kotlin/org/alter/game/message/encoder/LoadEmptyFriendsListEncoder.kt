package org.alter.game.message.encoder

import org.alter.game.message.MessageEncoder
import org.alter.game.message.impl.FriendListLoadedMessage

class LoadEmptyFriendsListEncoder : MessageEncoder<FriendListLoadedMessage>() {

    override fun extract(message: FriendListLoadedMessage, key: String): Number = throw Exception("Unhandled value key.")

    override fun extractBytes(message: FriendListLoadedMessage, key: String): ByteArray = throw Exception("Unhandled value key.")
}