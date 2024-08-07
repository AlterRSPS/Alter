package org.alter.game.saving.impl

import org.alter.game.model.appearance.Appearance
import org.alter.game.model.entity.Client
import org.alter.game.saving.DocumentHandler
import org.bson.Document

class AppearanceSerialisation(override val name: String = "appearance") : DocumentHandler {

    override fun fromDocument(client: Client, doc: Document) {
        client.appearance = Appearance.fromDocument(doc)
    }

    override fun asDocument(client: Client): Document {
        return client.appearance.asDocument()
    }

}