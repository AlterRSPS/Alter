package org.alter.game.saving.formats.impl

import com.mongodb.MongoException
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import io.github.oshai.kotlinlogging.KotlinLogging
import org.bson.Document

data class Environment(
    val database: String,
    val host: String,
    val port: Int = 27017,
    val username: String? = null,
    val password: String? = null,
    val options: String? = null
) {
    fun getConnectionLink(): String {
        return if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            "mongodb+srv://$host/$database"
        } else {
            "mongodb+srv://$username:$password@$host/$database?$options"
        }
    }
}

val CLOUD = Environment(
    database = "test",
    host = "test.t7jq1m5.mongodb.net",
    username = "admin123",
    password = System.getenv("MONGO_PASS"),
    options = "ssl=true&authSource=admin&retryWrites=true&w=majority"
)



object DatabaseManager {

    val logger = KotlinLogging.logger {}

    private var client: MongoClient? = null
    private var database: MongoDatabase? = null

    fun connect() {
        if (client != null) {
            return
        }
        logger.info { "Connecting to MongoDB" }

        try {
            client = MongoClients.create(CLOUD.getConnectionLink())
            database = client?.getDatabase(CLOUD.database)
                ?: throw MongoException("Failed to connect to MongoDB database ${CLOUD.database}.")
        } catch (e: MongoException) {
            throw MongoException("Failed to connect to MongoDB at ${CLOUD.host}:${CLOUD.port}.", e)
        }
    }

    /**
     * Gets a collection by name from the database.
     */
    fun getCollection(name: String): MongoCollection<Document> {
        return database?.getCollection(name)
            ?: throw MongoException("Database not connected. Cannot get collection.")
    }
}
