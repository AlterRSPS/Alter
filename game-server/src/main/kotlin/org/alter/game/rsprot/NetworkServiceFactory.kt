package org.alter.game.rsprot

import io.netty.buffer.PooledByteBufAllocator
import net.rsprot.compression.HuffmanCodec
import net.rsprot.compression.provider.DefaultHuffmanCodecProvider
import net.rsprot.compression.provider.HuffmanCodecProvider
import net.rsprot.crypto.rsa.RsaKeyPair
import net.rsprot.protocol.api.AbstractNetworkServiceFactory
import net.rsprot.protocol.api.GameConnectionHandler
import net.rsprot.protocol.api.bootstrap.BootstrapFactory
import net.rsprot.protocol.api.handlers.ExceptionHandlers
import net.rsprot.protocol.api.js5.Js5GroupProvider
import net.rsprot.protocol.api.suppliers.NpcInfoSupplier
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
import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcAvatarExceptionHandler
import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcIndexSupplier
import net.rsprot.protocol.message.codec.incoming.GameMessageConsumerRepositoryBuilder
import net.rsprot.protocol.message.codec.incoming.provider.DefaultGameMessageConsumerRepositoryProvider
import net.rsprot.protocol.message.codec.incoming.provider.GameMessageConsumerRepositoryProvider
import org.alter.game.message.handler.*
import org.alter.game.model.World
import org.alter.game.model.entity.Client
import org.alter.game.service.rsa.RsaService
import java.math.BigInteger

class NetworkServiceFactory(val world: World,
                            override val ports: List<Int>,
                            override val supportedClientTypes: List<OldSchoolClientType>
) : AbstractNetworkServiceFactory<Client, Js5GroupProvider.ByteBufJs5GroupType>() {

    override fun getBootstrapFactory(): BootstrapFactory {
        return BootstrapFactory(PooledByteBufAllocator.DEFAULT)
    }

    override fun getExceptionHandlers(): ExceptionHandlers<Client> {
        TODO("Not yet implemented")
    }

    override fun getGameConnectionHandler(): GameConnectionHandler<Client> {
        TODO("Not yet implemented")
    }

    override fun getGameMessageConsumerRepositoryProvider(): GameMessageConsumerRepositoryProvider<Client> {
        val bldr = GameMessageConsumerRepositoryBuilder<Client>();
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
        bldr.addListener(UpdatePlayerModel::class.java, UpdateAppearanceHandler())
        bldr.addListener(WindowStatus::class.java, WindowStatusHandler())
        bldr.addListener(ResumePObjDialog::class.java, ResumePObjDialogHandler())
        return DefaultGameMessageConsumerRepositoryProvider(bldr.build())
    }

    override fun getHuffmanCodecProvider(): HuffmanCodecProvider {
        val huffman: HuffmanCodec = world.huffmanNew
        return DefaultHuffmanCodecProvider(huffman)
    }

    override fun getJs5GroupProvider(): Js5GroupProvider<Js5GroupProvider.ByteBufJs5GroupType> {
        return DispleeJs5GroupProvider()
    }

    override fun getNpcInfoSupplier(): NpcInfoSupplier {
        val npcIndexSupplier: NpcIndexSupplier = RsmodNpcIndexSupplier(world)
        val npcAvatarExceptionHandler: NpcAvatarExceptionHandler = RsmodNpcAvatarExceptionHandler(world)
        return NpcInfoSupplier(npcIndexSupplier, npcAvatarExceptionHandler)
    }

    override fun getRsaKeyPair(): RsaKeyPair {
        val rsaService = world.getService(RsaService::class.java)!!
        val exponent: BigInteger = rsaService.getExponent()
        val modulus: BigInteger = rsaService.getModulus()
        return RsaKeyPair(exponent, modulus)
    }
}