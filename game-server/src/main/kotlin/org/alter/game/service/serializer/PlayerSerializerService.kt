package org.alter.game.service.serializer

import org.alter.game.Server
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.attr.APPEARANCE_SET_ATTR
import org.alter.game.model.attr.NEW_ACCOUNT_ATTR
import org.alter.game.model.entity.Client
import org.alter.game.service.Service
import gg.rsmod.net.codec.login.LoginRequest
import gg.rsmod.util.ServerProperties
import org.mindrot.jbcrypt.BCrypt

/**
 * A [Service] that is responsible for encoding and decoding player data.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class PlayerSerializerService : Service {

    private lateinit var startTile: Tile

    final override fun init(server: org.alter.game.Server, world: World, serviceProperties: ServerProperties) {
        startTile = Tile(world.gameContext.home)
        initSerializer(server, world, serviceProperties)
    }

    override fun postLoad(server: org.alter.game.Server, world: World) {
    }

    override fun bindNet(server: org.alter.game.Server, world: World) {
    }

    override fun terminate(server: org.alter.game.Server, world: World) {
    }

    fun configureNewPlayer(client: Client, request: LoginRequest) {
        client.attr.put(NEW_ACCOUNT_ATTR, true)
        client.attr.put(APPEARANCE_SET_ATTR, false)

        client.passwordHash = BCrypt.hashpw(request.password, BCrypt.gensalt(16))
        client.tile = startTile
    }

    abstract fun initSerializer(server: org.alter.game.Server, world: World, serviceProperties: ServerProperties)

    abstract fun loadClientData(client: Client, request: LoginRequest): PlayerLoadResult

    abstract fun saveClientData(client: Client): Boolean
}