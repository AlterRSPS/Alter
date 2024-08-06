package org.alter.game.playersaving.formats.impl.mongo

import com.mongodb.client.model.Filters.regex
import com.mongodb.client.model.Updates.set
import org.alter.game.model.entity.Client
import org.alter.game.playersaving.formats.FormatHandler
import org.bson.Document
import org.bson.conversions.Bson

class Mongo : FormatHandler() {

    private val COLLECTION_NAME = "players"

    override fun init() {
        DatabaseManager.connect()
    }

    override fun saveDocument(client: Client, document: Document) {
        if (!playerExists(client)) {
            DatabaseManager.getCollection(COLLECTION_NAME).insertOne(document)
        } else {
            val caseInsensitiveFilter = createCaseInsensitiveFilter(client)
            val attrs = document.get("attributes", Document::class.java)
            DatabaseManager.getCollection(COLLECTION_NAME).updateOne(caseInsensitiveFilter, set("attributes", attrs))
        }
    }

    override fun parseDocument(client : Client): Document {
        val caseInsensitiveFilter = createCaseInsensitiveFilter(client)
        return DatabaseManager.getCollection(COLLECTION_NAME).find(caseInsensitiveFilter).first()!!
    }

    override fun playerExists(client: Client): Boolean {
        val caseInsensitiveFilter = createCaseInsensitiveFilter(client)
        return DatabaseManager.getCollection(COLLECTION_NAME)
            .find(caseInsensitiveFilter)
            .toList()
            .isNotEmpty()
    }

    private fun createCaseInsensitiveFilter(client: Client): Bson {
        return regex("loginUsername", "^${client.loginUsername}$", "i")
    }
}