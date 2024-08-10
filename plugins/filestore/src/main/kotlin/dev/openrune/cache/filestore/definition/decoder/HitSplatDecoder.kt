package dev.openrune.cache.filestore.definition.decoder

import dev.openrune.cache.HITSPLAT
import dev.openrune.cache.filestore.definition.DefinitionDecoder
import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.definition.data.HitSplatType

class HitSplatDecoder : DefinitionDecoder<HitSplatType>(HITSPLAT) {

    override fun create(size: Int) = Array(size) { HitSplatType(it) }

    override fun getFile(id: Int) = id

    override fun HitSplatType.read(opcode: Int, buffer: Reader) {
        when (opcode) {
            1 -> font = buffer.readShortSmart()
            2 -> textColour = buffer.readUnsignedMedium()
            3 -> icon = buffer.readShortSmart()
            4 -> left = buffer.readShortSmart()
            5 -> middle = buffer.readShortSmart()
            6 -> right = buffer.readShortSmart()
            7 -> offsetX = buffer.readUnsignedShort()
            8 -> amount = buffer.readString()
            9 -> duration = buffer.readUnsignedShort()
            10 -> offsetY = buffer.readShort()
            11 -> fade = 0
            12 -> comparisonType = buffer.readUnsignedByte()
            13 -> damageYOfset = buffer.readShort()
            14 -> fade = buffer.readShort()
            17, 18 -> readTransforms(buffer, opcode == 18)
        }
    }
}