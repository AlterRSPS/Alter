package org.alter.game.playersaving

import org.alter.game.model.entity.Client
import org.bson.Document


interface DocumentDecoder {

    val name: String

    fun fromDocument(client: Client, doc: Document)
}