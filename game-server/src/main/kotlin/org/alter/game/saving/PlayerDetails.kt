package org.alter.game.saving

import io.github.oshai.kotlinlogging.KotlinLogging
import org.alter.game.GameContext
import org.alter.game.model.entity.Client
import org.alter.game.saving.formats.FormatHandler
import org.bson.Document
import java.util.concurrent.TimeUnit

data class DisplayName(
    var currentDisplayName: String = "",
    var previousDisplayName: String = "",
    var dateChanged: Long = -1
) {
    fun asDocument(): Document {
        return Document().apply {
            append("currentDisplayName", currentDisplayName)
            append("previousDisplayName", previousDisplayName)
            append("dateChanged", dateChanged)
        }
    }

    fun changeDisplayName(requestedName : String) {
        previousDisplayName = currentDisplayName
        currentDisplayName = requestedName
        dateChanged = System.currentTimeMillis()
    }

    companion object {
        fun fromDocument(doc: Document): DisplayName {
            return DisplayName(
                currentDisplayName = doc["currentDisplayName"] as String,
                previousDisplayName = doc["previousDisplayName"] as String,
                dateChanged = (doc["dateChanged"] as Number).toLong()
            )
        }
    }
}

object PlayerDetails {

    private val timeToNewNameChange = TimeUnit.SECONDS.toSeconds(24) * 1000

    private var displayNames: MutableMap<String, DisplayName> = mutableMapOf()

    private val logger = KotlinLogging.logger {}

    private lateinit var serialization : FormatHandler

    fun init(gameContext: GameContext) {
        serialization = gameContext.saveFormat.createInstance("accounts")

        //Loads Player details into memory so this can be searched fast (this should be low compared to loading saves 6k users will be about 8mb)?
        //this only needs to happen with json not with mongo
        serialization.loadAll().forEach {
            displayNames[it.key.lowercase()] = DisplayName.fromDocument(it.value)
        }

        logger.info { "Total Player Details Saved: ${displayNames.size}" }

        serialization.init()

    }

    fun playerExists(client: Client): Boolean {
        return serialization.playerExists(client) || !isNameAvailable(client.loginUsername)
    }


    private fun isNameAvailable(displayName: String): Boolean {
        if (displayNames.isEmpty()) {
            return true
        }

        val currentDate = System.currentTimeMillis()

        displayNames.values.forEach { (currentDisplayName, previousDisplayName, dateChanged) ->
            when {
                currentDisplayName.equals(displayName, ignoreCase = true) -> return false
                previousDisplayName.equals(displayName, ignoreCase = true) && (dateChanged.toInt() == -1 || currentDate - dateChanged <= timeToNewNameChange) -> return false
            }
        }

        return true
    }

    fun registerAccount(client: Client): Boolean {
        val displayName = DisplayName(client.loginUsername)
        displayNames[client.loginUsername.lowercase()] = displayName
        serialization.saveDocument(client,displayName.asDocument())
        return true
    }

    fun getDisplayName(loginName: String): DisplayName? {
        return displayNames[loginName.lowercase()]
    }

}
