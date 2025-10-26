package org.alter.game.saving

import io.github.oshai.kotlinlogging.KotlinLogging
import net.rsprot.crypto.xtea.XteaKey
import net.rsprot.protocol.loginprot.incoming.util.AuthenticationType
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock
import org.alter.game.GameContext
import org.alter.game.model.PlayerUID
import org.alter.game.model.attr.APPEARANCE_SET_ATTR
import org.alter.game.model.attr.NEW_ACCOUNT_ATTR
import org.alter.game.model.entity.Client
import org.alter.game.saving.impl.*
import org.alter.game.saving.formats.FormatHandler
import org.bson.Document
import org.mindrot.jbcrypt.BCrypt

object PlayerSaving {

    private val logger = KotlinLogging.logger {}

    lateinit var serialization : FormatHandler

    private val documents = linkedSetOf(
        DetailSerialisation(),
        AppearanceSerialisation(),
        SkillSerialisation(),
        AttributeSerialisation(),
        TimerSerialisation(),
        ContainersSerialisation(),
        VarpSerialisation(),
    )

    fun init(gameContext: GameContext) {
        serialization = gameContext.saveFormat.createInstance("details")

        logger.info { "Player Save Format : ${gameContext.saveFormat.name}" }

        serialization.init()

    }

    fun savePlayer(player: Client) {
        val doc = Document().apply {
            append("loginUsername", player.loginUsername)
            append("passwordHash", player.passwordHash)
            append("previousXteas", player.currentXteaKeys.asList())

            append("attributes", Document().also { attrs ->
                documents.forEach { encoder ->
                    attrs.append(encoder.name, encoder.asDocument(player))
                }
            })
        }
        serialization.saveDocument(player, doc)
    }

    fun loadPlayer(client: Client, block: LoginBlock<*>): PlayerLoadResult {
        if (!PlayerDetails.playerExists(client)) {
            val registered = PlayerDetails.registerAccount(client)
            if (!registered) {
                return PlayerLoadResult.INVALID_CREDENTIALS
            }
            configureNewPlayer(client, block)
            client.uid = PlayerUID(client.loginUsername)
            savePlayer(client)
            return PlayerLoadResult.NEW_ACCOUNT
        }

        return try {

            if (PlayerDetails.getDisplayName(client.loginUsername) == null) {
                return PlayerLoadResult.INVALID_CREDENTIALS
            }

            val document = serialization.parseDocument(client)
            client.loginUsername = document.getString("loginUsername")
            client.passwordHash = document.getString("passwordHash")
            val previousXteas = document.getList("previousXteas", Any::class.java).map { it as Int }.toIntArray()

            val authentication = validateAuthentication(previousXteas, client, block)
            if (authentication != PlayerLoadResult.LOAD_ACCOUNT) {
                return authentication
            }

            client.username = PlayerDetails.getDisplayName(client.loginUsername)?.currentDisplayName?: client.loginUsername

            client.uid = PlayerUID(client.username)

            if (!loadAttributes(client, document.get("attributes", Document::class.java))) {
                return PlayerLoadResult.MALFORMED
            }

            PlayerLoadResult.LOAD_ACCOUNT
        } catch (e: Exception) {
            logger.error(e) { "Error when loading player: ${client.loginUsername}" }
            PlayerLoadResult.MALFORMED
        }
    }

    private fun validateAuthentication(previousXteas : IntArray, client: Client, block: LoginBlock<*>): PlayerLoadResult {
        when (val auth = block.authentication) {
            is AuthenticationType.PasswordAuthentication -> {
                if (!BCrypt.checkpw(auth.password.asString(), client.passwordHash)) {
                    return PlayerLoadResult.INVALID_CREDENTIALS
                }
            }
            is XteaKey -> {
                if (!previousXteas.contentEquals(auth.key)) {
                    return PlayerLoadResult.INVALID_RECONNECTION
                }
            }
        }
        return PlayerLoadResult.LOAD_ACCOUNT
    }

    private fun loadAttributes(client: Client, attributes: Document?): Boolean {
        return try {
            attributes?.let {
                documents.forEach { decoder ->
                    attributes.get(decoder.name, Document::class.java)?.let { attrDoc ->
                        decoder.fromDocument(client, attrDoc)
                    } ?: return false
                }
            } ?: return false
            true
        } catch (e: Exception) {
            logger.error(e) { "Failed to decode attributes for client: ${client.loginUsername}" }
            false
        }
    }
    private fun configureNewPlayer(client: Client, block: LoginBlock<*>) {
        client.attr.put(NEW_ACCOUNT_ATTR, true)
        client.attr.put(APPEARANCE_SET_ATTR, false)

        if (block.authentication is AuthenticationType.PasswordAuthentication) {
            val passwordAuth = block.authentication as AuthenticationType.PasswordAuthentication
            client.passwordHash = BCrypt.hashpw(passwordAuth.password.asString(), BCrypt.gensalt(16))
        }
        client.tile = client.world.gameContext.home
    }
}
