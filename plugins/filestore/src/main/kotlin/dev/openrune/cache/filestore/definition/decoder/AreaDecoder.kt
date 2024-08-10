package dev.openrune.cache.filestore.definition.decoder

import dev.openrune.cache.AREA
import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.definition.DefinitionDecoder
import dev.openrune.cache.filestore.definition.data.AreaType

class AreaDecoder : DefinitionDecoder<AreaType>(AREA) {

    override fun create(size: Int) = Array(size) { AreaType(it) }

    override fun getFile(id: Int) = id

    override fun AreaType.read(opcode: Int, buffer: Reader) {
        when (opcode) {
            1 -> sprite1 = buffer.readLargeSmart()
            2 -> sprite2 = buffer.readLargeSmart()
            3 -> name = buffer.readString()
            4 -> fontColor = buffer.readMedium()
            5 -> buffer.readMedium()
            6 -> textSize = buffer.readUnsignedByte()
            7 -> {
                val size = buffer.readUnsignedByte()
                if ((size and 1) == 0) {
                    renderOnWorldMap = false
                }

                if ((size and 2) == 2) {
                    renderOnMinimap = true
                }
            }
            8 -> buffer.readUnsignedByte()
            in 10..14 -> options[opcode - 10] = buffer.readString()
            15 -> {
                val length: Int = buffer.readUnsignedByte()
                field1933 = MutableList(length * 2) { 0 }
                (0 until length * 2).forEach {
                    field1933!![it] = buffer.readShort()
                }
                buffer.readInt()
                val subLength: Int = buffer.readUnsignedByte()
                field1930 = MutableList(subLength) { 0 }
                (0 until subLength).forEach {
                    field1930[it] = buffer.readInt()
                }
                field1948 = MutableList(length) { 0 }
                (0 until length).forEach {
                    field1948[it] = buffer.readByte()
                }
            }
            16 -> buffer.readByte()
            17 -> menuTargetName = buffer.readString()
            18 -> buffer.readLargeSmart()
            19 -> category = buffer.readUnsignedShort()
            21 -> buffer.readInt()
            22 -> buffer.readInt()
            23 -> buffer.readMedium()
            24 -> {
                buffer.readShort()
                buffer.readShort()
            }
            25 -> buffer.readLargeSmart()
            28 -> buffer.readByte()
            29 -> horizontalAlignment = buffer.readUnsignedByte()
            30 -> verticalAlignment = buffer.readUnsignedByte()
        }
    }
}