package org.alter.game.service.serializer

import gg.rsmod.util.ServerProperties
import net.rsprot.protocol.loginprot.incoming.util.AuthenticationType
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.attr.APPEARANCE_SET_ATTR
import org.alter.game.model.attr.NEW_ACCOUNT_ATTR
import org.alter.game.model.entity.Client
import org.alter.game.service.Service
import org.mindrot.jbcrypt.BCrypt

/**
 * A [Service] that is responsible for encoding and decoding player data.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class PlayerSerializerService : Service {
    private lateinit var startTile: Tile

    final override fun init(
        server: org.alter.game.Server,
        world: World,
        serviceProperties: ServerProperties,
    ) {
        startTile = Tile(world.gameContext.home)
        initSerializer(server, world, serviceProperties)
    }

    fun configureNewPlayer(
        client: Client,
        block: LoginBlock<*>,
    ) {
        client.attr.put(NEW_ACCOUNT_ATTR, true)
        client.attr.put(APPEARANCE_SET_ATTR, false)

        if (block.authentication is AuthenticationType.PasswordAuthentication<*>) {
            client.passwordHash =
                BCrypt.hashpw(
                    (block.authentication as AuthenticationType.PasswordAuthentication<*>).password.asString(),
                    BCrypt.gensalt(16),
                )
        }
        client.tile = startTile
    }

    abstract fun initSerializer(
        server: org.alter.game.Server,
        world: World,
        serviceProperties: ServerProperties,
    )

    abstract fun loadClientData(
        client: Client,
        block: LoginBlock<*>,
    ): PlayerLoadResult

    abstract fun saveClientData(client: Client): Boolean
}
