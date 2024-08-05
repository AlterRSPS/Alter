package org.alter.game.playersaving

import io.github.oshai.kotlinlogging.KotlinLogging
import net.rsprot.crypto.xtea.XteaKey
import net.rsprot.protocol.loginprot.incoming.util.AuthenticationType
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock
import org.alter.game.model.PlayerUID
import org.alter.game.model.attr.APPEARANCE_SET_ATTR
import org.alter.game.model.attr.NEW_ACCOUNT_ATTR
import org.alter.game.model.entity.Client
import org.alter.game.playersaving.impl.DetailSerialisation
import org.alter.game.playersaving.impl.SkillSerialisation
import org.alter.game.playersaving.impl.VarpSerialisation
import org.alter.game.playersaving.seralizationTypes.impl.JsonSerialization
import org.bson.Document
import org.mindrot.jbcrypt.BCrypt

object PlayerSerialization {

    private val logger = KotlinLogging.logger {}

    private val serialization = JsonSerialization()

    private val documentDecoders = hashSetOf<DocumentDecoder>(
        DetailSerialisation(),
        VarpSerialisation(),
        SkillSerialisation()
    )

    private val documentEncoders = hashSetOf<DocumentEncoder>(
        DetailSerialisation(),
        VarpSerialisation(),
        SkillSerialisation()
    )

    fun savePlayer(player: Client) {
        val doc = Document().apply {
            append("displayName", player.username)
            append("loginUsername", player.loginUsername)
            append("passwordHash", player.passwordHash)
            append("previousXteas", player.currentXteaKeys.asList())
            append("attributes", Document().also { attrs ->
                documentEncoders.forEach { encoder ->
                    attrs.append(encoder.name, encoder.asDocument(player))
                }
            })
        }
        serialization.saveDocument(player, doc)
    }

    fun loadPlayer(client: Client, block: LoginBlock<*>): PlayerLoadResult {
        if (!serialization.playerExist(client.loginUsername)) {
            configureNewPlayer(client, block)
            client.uid = PlayerUID(client.loginUsername)
            savePlayer(client)
            return PlayerLoadResult.NEW_ACCOUNT
        }

        return try {
            val document = serialization.parseDocument(client)
            client.passwordHash = document.getString("passwordHash")
            val previousXteas = document.getList("previousXteas", Any::class.java).map { it as Int }.toIntArray()

            val authentication = validateAuthentication(previousXteas, client, block)
            if (authentication != PlayerLoadResult.LOAD_ACCOUNT) {
                return authentication
            }

            client.username = document.getString("displayName")
            client.loginUsername = document.getString("loginUsername")
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
            is AuthenticationType.PasswordAuthentication<*> -> {
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
                documentDecoders.forEach { decoder ->
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

        if (block.authentication is AuthenticationType.PasswordAuthentication<*>) {
            val passwordAuth = block.authentication as AuthenticationType.PasswordAuthentication<*>
            client.passwordHash = BCrypt.hashpw(passwordAuth.password.asString(), BCrypt.gensalt(16))
        }
        client.tile = client.world.gameContext.home
    }
}
