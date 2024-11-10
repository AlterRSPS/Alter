package org.alter.game.saving.impl

import org.alter.game.model.attr.AttributeKey
import org.alter.game.model.entity.Client
import org.alter.game.saving.DocumentHandler
import org.bson.Document

class AttributeSerialisation(override val name: String = "attribute") : DocumentHandler {

    override fun fromDocument(client: Client, doc: Document) {
        doc.forEach { key, value ->
            val attributeKey = AttributeKey<Any>(key)
            val processedValue = if (value is Double) value.toInt() else value
            client.attr[attributeKey] = processedValue
        }
    }

    override fun asDocument(client: Client): Document {
        return Document().apply {
            client.attr.toPersistentMap().forEach { (key, value) ->
                append(key, value)
            }
        }
    }
}