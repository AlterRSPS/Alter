package gg.rsmod.net.codec.login

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

/**
 * @author Tom <rspsmods@gmail.com>
 */
class LoginEncoder : MessageToByteEncoder<LoginResponse>() {

    override fun encode(ctx: ChannelHandlerContext, msg: LoginResponse, out: ByteBuf) {
        with(out) {
            writeByte(2)
            writeByte(29)
            writeByte(0)
            writeInt(0)
            writeByte( msg.privilege)
            writeBoolean (true) // pmod
            writeShort(msg.index)
            writeBoolean(true) // expanded friend list
            writeLong(0)
            writeLong(0)
        }
    }
}