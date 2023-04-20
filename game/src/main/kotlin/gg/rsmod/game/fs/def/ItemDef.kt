package gg.rsmod.game.fs.def

import gg.rsmod.game.fs.Definition
import gg.rsmod.util.io.BufferUtils.readString
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.bytes.Byte2ByteOpenHashMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.lang.IllegalStateException

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemDef(override val id: Int) : Definition(id) {

    var name = ""
    var model = -1
    var stacks = false
    var cost = 0
    var members = false
    val groundMenu = Array<String?>(5) { null }
    val inventoryMenu = Array<String?>(5) { null }
    val equipmentMenu = Array<String?>(8) { null }
    /**
     * The item can be traded through the grand exchange.
     */
    var isTradable = false
    var dropOptionIndex = -2
    var teamCape = 0
    var ambient = 0
    var contrast = 0

    /**
     * When an item is noted or unnoted (and has a noted variant), this will
     * represent the other item id. For example, item definition [4151] will
     * have a [noteLinkId] of [4152], while item definition [4152] will have
     * a [noteLinkId] of 4151.
     */
    var noteLinkId = 0
    /**
     * When an item is noted, it will set this value.
     */
    var noteTemplateId = 0
    var placeholderLink = 0
    var placeholderTemplate = 0
    var notedId = -1
    var unnotedId = -1
    val params = Int2ObjectOpenHashMap<Any>()
    var category = -1
    var zoom2d = 0

    /**
     * Custom metadata.
     */
    var examine: String? = null
    var tradeable = false
    var weight = 0.0
    var attackSpeed = -1
    var equipSlot = -1
    var equipType = 0
    var weaponType = -1
    var renderAnimations: IntArray? = null
    var skillReqs: Byte2ByteOpenHashMap? = null
    var equipSound: Int? = -1
    var femaleModel1 = 0
    var xan2d = 0
    var yan2d = 0
    var zan2d = 0
    var xOffset2d = 0
    var yOffset2d = 0
    var unknown1: String = ""
    var wearPos1: Int = -1
    var wearPos2: Int = -1
    var wearPos3: Int = -1

    /**
     * @TODO
     */
//    var maleModel0 =
//    var maleModel1 =
//
//
//    var maleOffset =
//    var femaleModel0 =
//    var femaleOffset: Int;


    lateinit var bonuses: IntArray

    val stackable: Boolean
        get() = stacks || noteTemplateId > 0

    val noted: Boolean
        get() = noteTemplateId > 0

    /**
     * Whether or not the object is a placeholder.
     */
    val isPlaceholder
        get() = placeholderTemplate > 0 && placeholderLink > 0

    override fun decode(buf: ByteBuf, opcode: Int) {
        when (opcode) {
            1 -> model = buf.readUnsignedShort()
            2 -> name = buf.readString()
            4 -> zoom2d = buf.readUnsignedShort()
            5 -> xan2d = buf.readUnsignedShort()
            6 -> yan2d = buf.readUnsignedShort()
            7 -> {
                xOffset2d = buf.readUnsignedShort()
                if (xOffset2d > 32767) {
                    xOffset2d -= 65536
                }
            }
            8 -> {
                yOffset2d = buf.readUnsignedShort()
                if (yOffset2d > 32767) {
                    yOffset2d -= 65536
                }
            }
            9 -> {
                unknown1 = buf.readString()
            }
            11 -> stacks = true
            12 -> cost = buf.readInt()
            13 -> wearPos1 = buf.readUnsignedByte().toInt()
            14 -> wearPos2 = buf.readUnsignedByte().toInt()
            16 -> members = true
            23 -> {
                /* maleModel0 = */ buf.readUnsignedShort()
                /* maleOffset = */ buf.readUnsignedByte()
            }
            24 -> /* maleModel1 = */ buf.readUnsignedShort()

            25 -> {
                /* femaleModel0 = */ buf.readUnsignedShort()
                /* femaleOffset = */ buf.readUnsignedByte()
            }
            26 -> femaleModel1 = buf.readUnsignedShort()
            27 -> wearPos3 = buf.readByte().toInt()

            in 30 until 35 -> {
                groundMenu[opcode - 30] = buf.readString()
                if (groundMenu[opcode - 30]!!.equals("null", true)
                    || groundMenu[opcode - 30]!!.equals("hidden", true)) {
                    groundMenu[opcode - 30] = null
                }
            }
            in 35 until 40 -> inventoryMenu[opcode - 35] = buf.readString()
            40 -> {
                val count = buf.readUnsignedByte()

                for (i in 0 until count) {
                    buf.readUnsignedShort()
                    buf.readUnsignedShort()
                }
            }
            41 -> {
                val count = buf.readUnsignedByte()

                for (i in 0 until count) {
                    buf.readUnsignedShort()
                    buf.readUnsignedShort()
                }
            }
            42 -> dropOptionIndex = buf.readByte().toInt()
            65 -> isTradable = true
            75 -> buf.readShort()
            78 -> buf.readUnsignedShort()
            79 -> buf.readUnsignedShort()
            90 -> buf.readUnsignedShort()
            91 -> buf.readUnsignedShort()
            92 -> buf.readUnsignedShort()
            93 -> buf.readUnsignedShort()
            94 -> category = buf.readUnsignedShort()
            95 -> buf.readUnsignedShort()
            97 -> noteLinkId = buf.readUnsignedShort()
            98 -> noteTemplateId = buf.readUnsignedShort()
            in 100 until 110 -> {
                buf.readUnsignedShort()
                buf.readUnsignedShort()
            }
            110 -> buf.readUnsignedShort()
            111 -> buf.readUnsignedShort()
            112 -> buf.readUnsignedShort()
            113 -> ambient = buf.readByte().toInt()
            114 -> contrast = buf.readByte().toInt() * 5
            115 -> teamCape = buf.readUnsignedByte().toInt()
            139 -> unnotedId = buf.readUnsignedShort()
            140 -> notedId = buf.readUnsignedShort()
            148 -> placeholderLink = buf.readUnsignedShort()
            149 -> placeholderTemplate = buf.readUnsignedShort()
            249 -> {
                params.putAll(readParams(buf))

                for (i in 0 until 8) {
                    val paramId = 451 + i
                    val option = params.get(paramId) as? String ?: continue
                    equipmentMenu[i] = option
                }
            }
            else -> throw IllegalStateException("Unknown opcode: $opcode in ItemDef")
        }
    }
}