package org.alter.game.protocol

import org.alter.game.message.Message
import org.alter.game.message.MessageEncoderSet
import org.alter.game.message.MessageStructureSet
import org.alter.game.message.impl.*
import gg.rsmod.net.packet.GamePacketBuilder
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder

import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * An implementation of [MessageToMessageEncoder] which is responsible for taking
 * the [Message] and converting it into a [org.alter.net.packet.GamePacket] so that
 * it may be written to a [io.netty.channel.Channel].
 *
 * @param encoders
 * The available [org.alter.game.message.MessageEncoder]s for the current
 * [org.alter.game.GameContext].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class GameMessageEncoder(private val encoders: MessageEncoderSet, private val structures: MessageStructureSet) : MessageToMessageEncoder<Message>() {

    override fun encode(ctx: ChannelHandlerContext, msg: Message, out: MutableList<Any>) {
        val encoder = encoders.get(msg.javaClass)
        val structure = structures.get(msg.javaClass)

        if (encoder == null) {
            logger.error("No encoder found for message $msg")
            return
        }

        if (structure == null) {
            logger.error("No packet structure found for message $msg")
            return
        }

        val builder = GamePacketBuilder(structure.opcodes.first(), structure.type)
        encoder.encode(msg, builder, structure)
        out.add(builder.toGamePacket())
    }

    companion object {
        private val logger = KotlinLogging.logger{}
    }
}