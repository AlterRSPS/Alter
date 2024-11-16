package org.alter.game.rsprot

import dev.openrune.cache.CacheManager
import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.DecoderException
import net.rsprot.compression.HuffmanCodec
import net.rsprot.compression.provider.DefaultHuffmanCodecProvider
import net.rsprot.compression.provider.HuffmanCodecProvider
import net.rsprot.crypto.rsa.RsaKeyPair
import net.rsprot.protocol.api.*
import net.rsprot.protocol.api.handlers.ExceptionHandlers
import net.rsprot.protocol.api.js5.Js5GroupProvider
import net.rsprot.protocol.api.suppliers.NpcInfoSupplier
import net.rsprot.protocol.api.suppliers.WorldEntityInfoSupplier
import net.rsprot.protocol.common.client.OldSchoolClientType
import net.rsprot.protocol.game.incoming.buttons.If1Button
import net.rsprot.protocol.game.incoming.buttons.If3Button
import net.rsprot.protocol.game.incoming.buttons.IfButtonD
import net.rsprot.protocol.game.incoming.buttons.IfButtonT
import net.rsprot.protocol.game.incoming.events.EventAppletFocus
import net.rsprot.protocol.game.incoming.events.EventCameraPosition
import net.rsprot.protocol.game.incoming.events.EventKeyboard
import net.rsprot.protocol.game.incoming.events.EventMouseClick
import net.rsprot.protocol.game.incoming.friendchat.FriendChatJoinLeave
import net.rsprot.protocol.game.incoming.locs.OpLoc
import net.rsprot.protocol.game.incoming.locs.OpLoc6
import net.rsprot.protocol.game.incoming.locs.OpLocT
import net.rsprot.protocol.game.incoming.messaging.MessagePrivate
import net.rsprot.protocol.game.incoming.messaging.MessagePublic
import net.rsprot.protocol.game.incoming.misc.client.DetectModifiedClient
import net.rsprot.protocol.game.incoming.misc.client.Idle
import net.rsprot.protocol.game.incoming.misc.client.MapBuildComplete
import net.rsprot.protocol.game.incoming.misc.client.WindowStatus
import net.rsprot.protocol.game.incoming.misc.user.*
import net.rsprot.protocol.game.incoming.npcs.OpNpc
import net.rsprot.protocol.game.incoming.npcs.OpNpc6
import net.rsprot.protocol.game.incoming.npcs.OpNpcT
import net.rsprot.protocol.game.incoming.objs.OpObj
import net.rsprot.protocol.game.incoming.objs.OpObj6
import net.rsprot.protocol.game.incoming.objs.OpObjT
import net.rsprot.protocol.game.incoming.players.OpPlayer
import net.rsprot.protocol.game.incoming.players.OpPlayerT
import net.rsprot.protocol.game.incoming.resumed.*
import net.rsprot.protocol.game.incoming.social.FriendListAdd
import net.rsprot.protocol.game.incoming.social.FriendListDel
import net.rsprot.protocol.game.incoming.social.IgnoreListAdd
import net.rsprot.protocol.game.incoming.social.IgnoreListDel
import net.rsprot.protocol.game.outgoing.info.worldentityinfo.WorldEntityAvatarExceptionHandler
import net.rsprot.protocol.message.IncomingGameMessage
import net.rsprot.protocol.message.codec.incoming.GameMessageConsumerRepositoryBuilder
import net.rsprot.protocol.message.codec.incoming.provider.DefaultGameMessageConsumerRepositoryProvider
import net.rsprot.protocol.message.codec.incoming.provider.GameMessageConsumerRepositoryProvider
import org.alter.game.message.handler.*
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.entity.Client
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Player
import org.alter.game.service.rsa.RsaService
import java.io.IOException
import java.math.BigInteger

class NetworkServiceFactory(
    val groupProvider: CacheJs5GroupProvider,
    val world: World,
    override val ports: List<Int>,
    override val supportedClientTypes: List<OldSchoolClientType>,
) : AbstractNetworkServiceFactory<Client>() {
/*    override fun getBootstrapFactory(): BootstrapFactory {
        return BootstrapFactory(PooledByteBufAllocator.DEFAULT)
    }*/

    override fun getExceptionHandlers(): ExceptionHandlers<Client> {
        val incomingGameMessageConsumerExceptionHandler: IncomingGameMessageConsumerExceptionHandler<Client> =
            IncomingGameMessageConsumerExceptionHandler {
                    session: Session<Client>, incomingGameMessage: IncomingGameMessage, throwable: Throwable ->
                throwable.printStackTrace()
            }
        return ExceptionHandlers(channelExceptionHandler(), incomingGameMessageConsumerExceptionHandler)
    }

    /**
     * @author Kris
     */
    private fun channelExceptionHandler(): ChannelExceptionHandler {
        return ChannelExceptionHandler { ctx: ChannelHandlerContext, cause: Throwable ->
            val channel = ctx.channel()
            if (channel.isOpen) {
                channel.close()
            }

            // IOExceptions basically all kinds of "connection dropped" type errors,
            // e.g. Closing the client, losing connection and so on...
            if (cause is IOException) return@ChannelExceptionHandler
            // Limit the decoder exceptions, so it doesn't just spam the error a thousand times if a thousand connections get rejected
            // Instead allow up to 100 to log, then turn it off. After that, only allow one more to log per tick
            if (cause is DecoderException) {
                return@ChannelExceptionHandler
            }

            logger.error{"Exception in netty handlers: $cause."}
        }
    }

    override fun getGameConnectionHandler(): GameConnectionHandler<Client> {
        return RsModConnectionHandler(world)
    }

    override fun getGameMessageConsumerRepositoryProvider(): GameMessageConsumerRepositoryProvider<Client> {
        val bldr = GameMessageConsumerRepositoryBuilder<Client>()
        bldr.addListener(DetectModifiedClient::class.java, DetectModifiedClientHandler())
        bldr.addListener(EventAppletFocus::class.java, EventAppletFocusHandler())
        bldr.addListener(EventCameraPosition::class.java, EventCameraPositionHandler())
        bldr.addListener(EventKeyboard::class.java, EventKeyboardHandler())
        bldr.addListener(FriendChatJoinLeave::class.java, ClanJoinChatLeaveHandler())
        bldr.addListener(MoveGameClick::class.java, ClickMapHandler())
        bldr.addListener(MoveMinimapClick::class.java, ClickMinimapHandler())
        bldr.addListener(ClickWorldMap::class.java, ClickWorldMapHandler())
        bldr.addListener(ClientCheat::class.java, ClientCheatHandler())
        bldr.addListener(CloseModal::class.java, CloseMainComponentHandler())
        bldr.addListener(Idle::class.java, EventMouseIdleHandler())
        bldr.addListener(FriendListAdd::class.java, FriendListAddHandler())
        bldr.addListener(FriendListDel::class.java, FriendListDeleteHandler())
        bldr.addListener(If3Button::class.java, IfButton1Handler())
        bldr.addListener(IfButtonD::class.java, IfButtonDHandler())
        bldr.addListener(IfButtonT::class.java, IfButtonTHandler())
        bldr.addListener(If1Button::class.java, IfModelOp1Handler())
        bldr.addListener(IgnoreListAdd::class.java, IgnoreListAddHandler())
        bldr.addListener(IgnoreListDel::class.java, IgnoreListDeleteHandler())
        bldr.addListener(EventMouseClick::class.java, EventMouseClickHandler())
        bldr.addListener(MessagePrivate::class.java, MessagePrivateSenderHandler())
        bldr.addListener(MessagePublic::class.java, MessagePublicHandler())
        bldr.addListener(OpLoc6::class.java, OpLoc6Handler())
        bldr.addListener(OpLoc::class.java, OpLocHandler())
        bldr.addListener(OpLocT::class.java, OpLocTHandler())
        bldr.addListener(OpNpc6::class.java, OpNpc6Handler())
        bldr.addListener(OpNpc::class.java, OpNpcHandler())
        bldr.addListener(OpNpcT::class.java, OpNpcTHandler())
        bldr.addListener(MapBuildComplete::class.java, MapBuildCompleteHandler())
        bldr.addListener(OpPlayer::class.java, OpPlayerHandler())
        bldr.addListener(OpPlayerT::class.java, OpPlayerTHandler())
        bldr.addListener(ResumePauseButton::class.java, ResumePauseButtonHandler())
        bldr.addListener(ResumePCountDialog::class.java, ResumePCountDialogHandler())
        bldr.addListener(ResumePNameDialog::class.java, ResumePNameDialogHandler())
        bldr.addListener(OpObj6::class.java, OpObj6Handler())
        bldr.addListener(OpObj::class.java, OpObjHandler())
        bldr.addListener(OpObjT::class.java, OpObjTHandler())
        bldr.addListener(ResumePStringDialog::class.java, ResumePStringDialogHandler())
        bldr.addListener(Teleport::class.java, TeleportHandler())
        bldr.addListener(WindowStatus::class.java, WindowStatusHandler())
        bldr.addListener(ResumePObjDialog::class.java, ResumePObjDialogHandler())
        return DefaultGameMessageConsumerRepositoryProvider(bldr.build())
    }

    override fun getHuffmanCodecProvider(): HuffmanCodecProvider {
        val huffman: HuffmanCodec = HuffmanCodec.create(Unpooled.wrappedBuffer(CacheManager.cache.data(10, 1)!!))
        return DefaultHuffmanCodecProvider(huffman)
    }

    override fun getJs5GroupProvider(): Js5GroupProvider {
        return groupProvider
    }

    override fun getRsaKeyPair(): RsaKeyPair {
        val rsaService = world.getService(RsaService::class.java)!! // evil assertion ;/
        val exponent: BigInteger = rsaService.getExponent()
        val modulus: BigInteger = rsaService.getModulus()
        return RsaKeyPair(exponent, modulus)
    }

    private fun shouldAdd(
        player: Player,
        npc: Npc,
        viewDistance: Int,
    ): Boolean =
        npc.isSpawned() && !npc.invisible &&
            npc.tile.isWithinRadius(
                player.tile,
                viewDistance,
            ) && (npc.owner == null || npc.owner == player)

    companion object {
        private val logger = KotlinLogging.logger {}

    }
}
