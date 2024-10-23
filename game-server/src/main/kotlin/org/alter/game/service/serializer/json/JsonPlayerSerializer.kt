package org.alter.game.service.serializer.json

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import gg.rsmod.util.ServerProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import net.rsprot.crypto.xtea.XteaKey
import net.rsprot.protocol.loginprot.incoming.util.AuthenticationType
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock
import org.alter.game.model.PlayerUID
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.appearance.Appearance
import org.alter.game.model.appearance.Gender
import org.alter.game.model.attr.AttributeKey
import org.alter.game.model.container.ItemContainer
import org.alter.game.model.entity.Client
import org.alter.game.model.interf.DisplayMode
import org.alter.game.model.item.Item
import org.alter.game.model.priv.Privilege
import org.alter.game.model.social.Social
import org.alter.game.model.timer.TimerKey
import org.alter.game.service.serializer.PlayerLoadResult
import org.alter.game.service.serializer.PlayerSerializerService
import org.mindrot.jbcrypt.BCrypt
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * A [PlayerSerializerService] implementation that decodes and encodes player
 * data in JSON.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class JsonPlayerSerializer : PlayerSerializerService() {
    private lateinit var path: Path

    override fun initSerializer(
        server: org.alter.game.Server,
        world: World,
        serviceProperties: ServerProperties,
    ) {
        path = Paths.get(serviceProperties.getOrDefault("path", "../data/saves/"))
        if (!Files.exists(path)) {
            Files.createDirectory(path)
            logger.info { "Path does not exist: $path, creating directory..." }
        }
    }

    override fun loadClientData(
        client: Client,
        block: LoginBlock<*>,
    ): PlayerLoadResult {
        val save = path.resolve(client.loginUsername)
        if (!Files.exists(save)) {
            configureNewPlayer(client, block)
            client.uid = PlayerUID(client.loginUsername)
            saveClientData(client)
            return PlayerLoadResult.NEW_ACCOUNT
        }
        try {
            val world = client.world
            val reader = Files.newBufferedReader(save)
            val json = Gson()
            val data = json.fromJson(reader, JsonPlayerSaveData::class.java)
            reader.close()

            if (block.authentication is AuthenticationType.PasswordAuthentication<*>) {
                /*
                 * If the [request] is not a [LoginRequest.reconnecting] request, we have to
                 * verify the password is correct.
                 */
                if (!BCrypt.checkpw(
                        (block.authentication as AuthenticationType.PasswordAuthentication<*>).password.asString(),
                        data.passwordHash,
                    )
                ) {
                    return PlayerLoadResult.INVALID_CREDENTIALS
                }
            } else {
                if (block.authentication is XteaKey) {
                    /*
                     * If the [request] is a [LoginRequest.reconnecting] request, we
                     * verify that the login xteas match from our previous session.
                     */
                    if (!Arrays.equals(data.previousXteas, (block.authentication as XteaKey).key)) {
                        return PlayerLoadResult.INVALID_RECONNECTION
                    }
                }
            }

            client.loginUsername = data.username
            client.uid = PlayerUID(data.username)
            client.username = data.displayName
            client.passwordHash = data.passwordHash
            client.tile = Tile(data.x, data.y, data.height)
            client.privilege = world.privileges.get(data.privilege) ?: Privilege.DEFAULT
            client.runEnergy = data.runEnergy
            client.interfaces.displayMode = DisplayMode.values.firstOrNull { it.id == data.displayMode } ?: DisplayMode.FIXED
            client.appearance =
                Appearance(
                    data.appearance.looks,
                    data.appearance.colors,
                    Gender.values.firstOrNull {
                        it.id == data.appearance.gender
                    } ?: Gender.MALE,
                )
            data.skills.forEach { skill ->
                client.getSkills().setXp(skill.skill, skill.xp)
                client.getSkills().setCurrentLevel(skill.skill, skill.lvl)
            }
            data.itemContainers.forEach {
                val key = world.plugins.containerKeys.firstOrNull { other -> other.name == it.name }
                if (key == null) {
                    logger.error { "Container was found in serialized data, but is not registered to our World. [key=${it.name}]" }
                    return@forEach
                }
                val container =
                    if (client.containers.containsKey(key)) {
                        client.containers[key]
                    } else {
                        client.containers[key] = ItemContainer(key)
                        client.containers[key]
                    }!!
                it.items.forEach { slot, item ->
                    container[slot] = item
                }
            }
            data.attributes.forEach { (key, value) ->
                val attribute = AttributeKey<Any>(key)
                client.attr[attribute] = if (value is Double) value.toInt() else value
            }
            data.timers.forEach { timer ->
                var time = timer.timeLeft
                if (timer.tickOffline) {
                    val elapsed = System.currentTimeMillis() - timer.currentMs
                    val ticks = (elapsed / client.world.gameContext.cycleTime).toInt()
                    time -= ticks
                }
                val key = TimerKey(timer.identifier, timer.tickOffline)
                client.timers[key] = Math.max(0, time)
            }
            data.varps.forEach { varp ->
                client.varps.setState(varp.id, varp.state)
            }

            if (data.social == null) {
                data.social = Social()
            }

            client.social = data.social

            return PlayerLoadResult.LOAD_ACCOUNT
        } catch (e: Exception) {
            logger.error(e) { "Error when loading player: ${block.username}" }
            return PlayerLoadResult.MALFORMED
        }
    }

    override fun saveClientData(client: Client): Boolean {
        val data =
            JsonPlayerSaveData(
                passwordHash = client.passwordHash,
                username = client.loginUsername,
                previousXteas = client.currentXteaKeys,
                displayName = client.username,
                x = client.tile.x,
                y = client.tile.y,
                height = client.tile.height,
                privilege = client.privilege.id,
                runEnergy = client.runEnergy,
                displayMode = client.interfaces.displayMode.id,
                appearance = client.getPersistentAppearance(),
                skills = client.getPersistentSkills(),
                itemContainers = client.getPersistentContainers(),
                attributes = client.attr.toPersistentMap(),
                timers = client.timers.toPersistentTimers(),
                varps = client.varps.getAll().filter { it.state != 0 },
                social = client.social,
            )
        val writer = Files.newBufferedWriter(path.resolve(client.loginUsername))
        val json = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
        json.toJson(data, writer)
        writer.close()
        return true
    }

    private fun Client.getPersistentContainers(): List<PersistentContainer> {
        val persistent = mutableListOf<PersistentContainer>()

        containers.forEach { (key, container) ->
            if (!container.isEmpty) {
                persistent.add(PersistentContainer(key.name, container.toMap()))
            }
        }

        return persistent
    }

    private fun Client.getPersistentSkills(): List<PersistentSkill> {
        val skills = mutableListOf<PersistentSkill>()

        for (i in 0 until getSkills().maxSkills) {
            val xp = getSkills().getCurrentXp(i)
            val lvl = getSkills().getCurrentLevel(i)

            skills.add(PersistentSkill(skill = i, xp = xp, lvl = lvl))
        }

        return skills
    }

    private fun Client.getPersistentAppearance(): PersistentAppearance =
        PersistentAppearance(appearance.gender.id, appearance.looks, appearance.colors)

    data class PersistentAppearance(
        @JsonProperty("gender") val gender: Int,
        @JsonProperty("looks") val looks: IntArray,
        @JsonProperty("colors") val colors: IntArray,
    )

    data class PersistentContainer(
        @JsonProperty("name") val name: String,
        @JsonProperty("items") val items: Map<Int, Item>,
    )

    data class PersistentSkill(
        @JsonProperty("skill") val skill: Int,
        @JsonProperty("xp") val xp: Double,
        @JsonProperty("lvl") val lvl: Int,
    )

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
