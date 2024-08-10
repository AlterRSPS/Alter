package dev.openrune.cache.filestore.definition.decoder

import dev.openrune.cache.HEALTHBAR
import dev.openrune.cache.filestore.definition.DefinitionDecoder
import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.definition.data.HealthBarType

class HealthBarDecoder : DefinitionDecoder<HealthBarType>(HEALTHBAR) {

    override fun create(size: Int) = Array(size) { HealthBarType(it) }

    override fun getFile(id: Int) = id

    override fun HealthBarType.read(opcode: Int, buffer: Reader) {
        when (opcode) {
            1 -> buffer.readUnsignedShort()
            2 -> int1 = buffer.readUnsignedByte()
            3 -> int2 = buffer.readUnsignedByte()
            4 -> int3 = 0
            5 -> int4 = buffer.readUnsignedShort()
            6 -> buffer.readUnsignedByte()
            7 -> frontSpriteId = buffer.readUnsignedShort()
            8 -> backSpriteId = buffer.readUnsignedShort()
            11 -> int3 = buffer.readUnsignedShort()
            14 -> width = buffer.readUnsignedByte()
            15 -> widthPadding = buffer.readUnsignedByte()
        }
    }
}