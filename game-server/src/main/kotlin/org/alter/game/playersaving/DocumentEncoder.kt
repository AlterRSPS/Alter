package org.alter.game.playersaving

import org.alter.game.model.entity.Client
import org.bson.Document

interface DocumentEncoder {

    val name: String

    fun asDocument(client: Client) : Document
}