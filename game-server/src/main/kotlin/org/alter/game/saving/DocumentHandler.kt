package org.alter.game.saving

import org.alter.game.model.entity.Client
import org.bson.Document

interface DocumentHandler {

    val name: String

    fun asDocument(client: Client) : Document

    fun fromDocument(client: Client, doc: Document)

}