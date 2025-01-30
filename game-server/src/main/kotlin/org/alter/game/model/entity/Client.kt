package org.alter.game.model.entity

import gg.rsmod.util.toStringHelper
import net.rsprot.protocol.api.login.GameLoginResponseHandler
import net.rsprot.protocol.game.outgoing.map.RebuildLogin
import net.rsprot.protocol.game.outgoing.map.RebuildNormal
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock
import net.rsprot.protocol.message.OutgoingGameMessage
import org.alter.game.model.EntityType
import org.alter.game.model.World
import org.alter.game.saving.PlayerSaving

/**
 * A [Player] that is controlled by a human. A [Client] is responsible for
 * handling any network related job.
 *
 * Anything other than network logic should be added to [Player] instead.
 *
 * @param world
 * The [World] that this client is registered to.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Client(world: World) : Player(world) {
    /**
     * The username that was used to register the [Player]. This username should
     * never be changed through the player's end.
     */
    lateinit var loginUsername: String

    /**
     * The encrypted password.
     */
    lateinit var passwordHash: String

    /**
     * The client's UUID.
     */
    lateinit var uuid: String

    /**
     * The xteas for the current log-in session.
     */
    lateinit var currentXteaKeys: IntArray

    /**
     * Is the applet focused on the player's computer?
     */
    var appletFocused = true

    /**
     * The applet's current width.
     */
    var clientWidth = 765

    /**
     * The applet's current height.
     */
    var clientHeight = 503

    /**
     * The pitch of the camera in the client's game UI.
     */
    var cameraPitch = 0

    /**
     * The yaw of the camera in the client's game UI.
     */
    var cameraYaw = 0

    /**
     * A flag which indicates that the client will have their incoming packets
     * ([org.alter.game.message.Message]s) logged.
     */
    var logPackets = true

    override val entityType: EntityType = EntityType.CLIENT

    override fun handleLogout() {
        super.handleLogout()
        PlayerSaving.savePlayer(this)
    }

    override fun handleMessages() {
        session?.processIncomingPackets(this)
    }

    private var rebuildNormalMessageWritten = false
    private val pendingMessages = mutableListOf<OutgoingGameMessage>()

    private fun onRebuildNormalMessageWritten() {
        pendingMessages.forEach { message ->
            session?.queue(message)
        }
        pendingMessages.clear()
    }

    override fun write(vararg messages: OutgoingGameMessage) {
        messages.forEach { message ->
            if (!rebuildNormalMessageWritten && (message is RebuildNormal || message is RebuildLogin)) {
                session?.queue(message)
                rebuildNormalMessageWritten = true
                onRebuildNormalMessageWritten()
            } else if (rebuildNormalMessageWritten) {
                session?.queue(message)
            } else {
                pendingMessages.add(message)
            }
        }
    }

    override fun channelFlush() {
        session?.flush()
    }

    override fun channelClose() {
        world.network.playerInfoProtocol.dealloc(info = playerInfo)
    }

    override fun toString(): String =
        toStringHelper()
            .add("login_username", loginUsername)
            .add("username", username)
            .toString()

    companion object {
        /**
         * Constructs a [Client] based on the [LoginRequest].
         */
        fun fromRequest(
            world: World,
            request: GameLoginResponseHandler<Client>,
            block: LoginBlock<*>,
        ): Client {
            val client = Client(world)
            client.clientWidth = block.width
            client.clientHeight = block.height
            client.loginUsername = block.username
            client.username = block.username
            client.uuid = block.uuid.toString()
            client.currentXteaKeys = block.seed
            return client
        }
    }
}
