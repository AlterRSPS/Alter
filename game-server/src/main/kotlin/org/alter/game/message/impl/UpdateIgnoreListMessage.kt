package org.alter.game.message.impl

import org.alter.game.message.Message

data class UpdateIgnoreListMessage(val online: Number, val username: String, val previousUsername: String) : Message