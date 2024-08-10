package dev.openrune.cache.filestore.definition.decoder

import dev.openrune.cache.ITEM
import dev.openrune.cache.filestore.definition.DefinitionDecoder
import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.definition.data.ItemType

class ItemDecoder : DefinitionDecoder<ItemType>(ITEM) {
    override fun create(size: Int) = Array(size) { ItemType(it) }

    override fun getFile(id: Int) = id

    override fun ItemType.read(opcode: Int, buffer: Reader) {
        when (opcode) {
            1 -> inventoryModel = buffer.readUnsignedShort()
            2 -> name = buffer.readString()
            3 -> examine = buffer.readString()
            4 -> zoom2d = buffer.readUnsignedShort()
            5 -> xan2d = buffer.readUnsignedShort()
            6 -> yan2d = buffer.readUnsignedShort()
            7 -> xOffset2d = buffer.readUnsignedShort()
            8 -> yOffset2d = buffer.readUnsignedShort()
            11 -> stacks = 1
            12 -> cost = buffer.readInt()
            13 -> equipSlot = buffer.readUnsignedByte()
            14 -> appearanceOverride1 = buffer.readUnsignedByte()
            16 -> members = true
            23 -> {
                maleModel0 = buffer.readUnsignedShort()
                maleOffset = buffer.readUnsignedByte()
            }
            24 -> maleModel1 = buffer.readUnsignedShort()
            25 -> {
                femaleModel0 = buffer.readUnsignedShort()
                femaleOffset = buffer.readUnsignedByte()
            }
            26 -> femaleModel1 = buffer.readUnsignedShort()
            27 -> appearanceOverride2 = buffer.readByte()
            in 30..34 -> options[opcode - 30] = buffer.readString()
            in 35..39 -> interfaceOptions[opcode - 35] = buffer.readString()
            40 -> readColours(buffer)
            41 -> readTextures(buffer)
            42 -> dropOptionIndex = buffer.readByte()
            65 -> isTradeable = true
            75 -> weight = buffer.readUnsignedShort().toDouble()
            78 -> maleModel2 = buffer.readUnsignedShort()
            79 -> femaleModel2 = buffer.readUnsignedShort()
            90 -> maleHeadModel0 = buffer.readUnsignedShort()
            91 -> femaleHeadModel0 = buffer.readUnsignedShort()
            92 -> maleHeadModel1 = buffer.readUnsignedShort()
            93 -> femaleHeadModel1 = buffer.readUnsignedShort()
            94 -> category = buffer.readUnsignedShort()
            95 -> zan2d = buffer.readUnsignedShort()
            97 -> noteLinkId = buffer.readUnsignedShort()
            98 -> noteTemplateId = buffer.readUnsignedShort()
            in 100..109 -> {
                countObj[opcode - 100] = buffer.readUnsignedShort()
                countCo[opcode - 100] = buffer.readUnsignedShort()
            }
            110 -> resizeX = buffer.readUnsignedShort()
            111 -> resizeY = buffer.readUnsignedShort()
            112 -> resizeZ = buffer.readUnsignedShort()
            113 -> ambient = buffer.readByte()
            114 -> contrast = buffer.readByte()
            115 -> teamCape = buffer.readByte()
            139 -> unnotedId = buffer.readUnsignedShort()
            140 -> notedId = buffer.readUnsignedShort()
            148 -> placeholderLink = buffer.readUnsignedShort()
            149 -> placeholderTemplate = buffer.readUnsignedShort()
            249 -> readParameters(buffer)
            else -> logger.info { "Unable to decode Npcs [${opcode}]" }
        }
    }

}