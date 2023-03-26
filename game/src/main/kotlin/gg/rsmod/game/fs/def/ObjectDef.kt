package gg.rsmod.game.fs.def

import gg.rsmod.game.fs.Definition
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.util.io.BufferUtils.readString
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.lang.IllegalStateException

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ObjectDef(override val id: Int) : Definition(id) {

    var name = ""
    var width = 1
    var length = 1
    var solid = true
    var blockProjectile = true
    var interactType = false
    var contouredGround: Int = -1
    var nonFlatShading = false
    var clippedModel = false
    var obstructive = false
    var hollow = false
    var supportItems = -1
    var clipMask = 0
    var varbit = -1
    var varp = -1
    var animation = -1
    var clipType = 2
    var rotated = false
    var clipped = true
    var category = -1
    val options: Array<String?> = Array(5) { "" }
    var transforms: Array<Int>? = null
    val params = Int2ObjectOpenHashMap<Any>()
    var examine: String? = null
    var mapIconId = -1
    var aBoolean3429 = true

    fun getRotatedWidth(obj: GameObject): Int = when {
        (obj.rot and 0x1) == 1 -> length
        else -> width
    }

    fun getRotatedLength(obj: GameObject): Int = when {
        (obj.rot and 0x1) == 1 -> width
        else -> length
    }

    override fun decode(buf: ByteBuf, opcode: Int) {
        when (opcode) {
            1 -> {
                val count = buf.readUnsignedByte()
                for (i in 0 until count) {
                    buf.readUnsignedShort() // Model
                    buf.readUnsignedByte() // Model type
                }
            }
            2 -> name = buf.readString()
            5 -> {
                val count = buf.readUnsignedByte()
                for (i in 0 until count) {
                    buf.readUnsignedShort() // Model
                }
            }
            14 -> width = buf.readUnsignedByte().toInt()
            15 -> length = buf.readUnsignedByte().toInt()
            17 -> solid = false // interact type = 0
            18 -> blockProjectile = false
            19 -> interactType = buf.readUnsignedByte().toInt() == 1
            21 -> contouredGround = 0
            22 -> nonFlatShading = true
            23 -> clippedModel = true
            24 -> {
                animation = buf.readUnsignedShort()
                if (animation == 65535) animation = -1

            }
            27 -> clipType = 1
            28 -> buf.readUnsignedByte()
            29 -> buf.readByte()
            in 30 until 35 -> {
                options[opcode - 30] = buf.readString()
                if (options[opcode - 30].equals("null", true)
                    || options[opcode - 30].equals("hidden", true)) {
                    options[opcode - 30] = null
                }
            }
            39 -> buf.readByte()
            40 -> {
                val count = buf.readUnsignedByte()
                for (i in 0 until count) {
                    buf.readUnsignedShort() // Recolor src
                    buf.readUnsignedShort() // Recolor dst
                }
            }
            41 -> {
                val count = buf.readUnsignedByte()
                for (i in 0 until count) {
                    buf.readUnsignedShort() // Retexture src
                    buf.readUnsignedShort() // Retexture dst
                }
            }
            61 -> category = buf.readUnsignedShort()
            62 -> rotated = true
            64 -> clipped = false
            65 -> buf.readUnsignedShort()
            66 -> buf.readUnsignedShort()
            67 -> buf.readUnsignedShort()
            68 -> buf.readUnsignedShort()
            69 -> clipMask = buf.readUnsignedByte().toInt()
            70 -> buf.readShort()
            71 -> buf.readShort()
            72 -> buf.readShort()
            73 -> obstructive = true
            74 -> hollow = true
            75 -> supportItems = buf.readUnsignedByte().toInt()
            77, 92 -> {
                varbit = buf.readUnsignedShort()
                if (varbit == 65535) varbit = -1

                varp = buf.readUnsignedShort()
                if (varp == 65535) varp = -1

                var terminatingTransform = -1
                if (opcode == 92) {
                    terminatingTransform = buf.readUnsignedShort()
                    if(terminatingTransform == 65535) {
                        terminatingTransform = -1
                    }
                }

                val count = buf.readUnsignedByte().toInt()
                transforms = Array(count + 2) { 0 }

                for (i in 0..count) {
                    var transform = buf.readUnsignedShort()
                    if(transform == 65535){
                        transform = -1
                    }
                    transforms!![i] = transform
                }

                transforms!![count+1] = terminatingTransform
            }
            78 -> {
                buf.readUnsignedShort()
                buf.readUnsignedByte()
            }
            79 -> {
                buf.readUnsignedShort()
                buf.readUnsignedShort()
                buf.readUnsignedByte()
                val count = buf.readUnsignedByte().toInt()
                for (i in 0 until count) {
                    buf.readUnsignedShort()
                }
            }
            81 -> contouredGround = buf.readUnsignedByte() * 256
            82 -> mapIconId = buf.readUnsignedShort()
            89 -> aBoolean3429 = false
            249 -> params.putAll(readParams(buf))
            else -> throw IllegalStateException("Unknown opcode: $opcode in ObjectDef")
        }
    }
}