package dev.openrune.cache.filestore.definition.encoder

import dev.openrune.cache.filestore.buffer.Writer
import dev.openrune.cache.filestore.definition.ConfigEncoder
import dev.openrune.cache.filestore.definition.data.ItemType

class ItemEncoder : ConfigEncoder<ItemType>() {

    override fun Writer.encode(definition: ItemType) {
        if (definition.inventoryModel != 0) {
            writeByte(1)
            writeShort(definition.inventoryModel)
        }

        if (!definition.name.equals("null", ignoreCase = true)) {
            writeByte(2)
            writeString(definition.name)
        }

        if (!definition.name.equals("null", ignoreCase = true)) {
            writeByte(3)
            writeString(definition.examine)
        }

        if (definition.zoom2d != 2000) {
            writeByte(4)
            writeShort(definition.zoom2d)
        }

        if (definition.xan2d != 0) {
            writeByte(5)
            writeShort(definition.xan2d)
        }

        if (definition.yan2d != 0) {
            writeByte(6)
            writeShort(definition.yan2d)
        }

        if (definition.xOffset2d != 0) {
            writeByte(7)
            writeShort(definition.xOffset2d)
        }

        if (definition.yOffset2d != 0) {
            writeByte(8)
            writeShort(definition.yOffset2d)
        }

        if (definition.stackable) {
            writeByte(11)
        }

        if (definition.cost != 1) {
            writeByte(12)
            writeInt(definition.cost)
        }

        if (definition.equipSlot != -1) {
            writeByte(13)
            writeByte(definition.equipSlot)
        }

        if (definition.appearanceOverride1 != -1) {
            writeByte(14)
            writeByte(definition.appearanceOverride1)
        }

        if (definition.members) {
            writeByte(16)
        }

        if (definition.maleModel0 != -1 || definition.maleOffset != 0) {
            writeByte(23)
            writeShort(definition.maleModel0)
            writeByte(definition.maleOffset)
        }

        if (definition.maleModel1 != -1) {
            writeByte(24)
            writeShort(definition.maleModel1)
        }

        if (definition.femaleModel0 != -1 || definition.femaleOffset != 0) {
            writeByte(25)
            writeShort(definition.femaleModel0)
            writeByte(definition.femaleOffset)
        }

        if (definition.femaleModel1 != -1) {
            writeByte(26)
            writeShort(definition.femaleModel1)
        }

        if (definition.appearanceOverride2 != 0) {
            writeByte(27)
            writeByte(definition.appearanceOverride2)
        }

        if (definition.options != mutableListOf(null, null, "Take", null, null)) {
            for (i in 0 until definition.options.size) {
                if (definition.options[i] == null) {
                    continue
                }
                writeByte(i + 30)
                writeString(definition.options[i]!!)
            }
        }

        if (definition.interfaceOptions != mutableListOf(null, null, null, null, "Drop")) {
            for (i in 0 until definition.interfaceOptions.size) {
                if (definition.interfaceOptions[i] == null) {
                    continue
                }
                writeByte(i + 35)
                writeString(definition.interfaceOptions[i]!!)
            }
        }

        definition.writeColoursTextures(this)

        if (definition.dropOptionIndex != -2) {
            writeByte(42)
            writeByte(definition.dropOptionIndex)
        }

        if (definition.isTradeable) {
            writeByte(65)
        }

        if (definition.weight != 0.0) {
            writeByte(75)
            writeShort(definition.weight.toInt())
        }

        if (definition.maleModel2 != -1) {
            writeByte(78)
            writeShort(definition.maleModel2)
        }

        if (definition.femaleModel2 != -1) {
            writeByte(79)
            writeShort(definition.femaleModel2)
        }

        if (definition.maleHeadModel0 != -1) {
            writeByte(90)
            writeShort(definition.maleHeadModel0)
        }

        if (definition.femaleHeadModel0 != -1) {
            writeByte(91)
            writeShort(definition.femaleHeadModel0)
        }

        if (definition.maleHeadModel1 != -1) {
            writeByte(92)
            writeShort(definition.maleHeadModel1)
        }

        if (definition.femaleHeadModel1 != -1) {
            writeByte(93)
            writeShort(definition.femaleHeadModel1)
        }

        if (definition.category != -1) {
            writeByte(94)
            writeShort(definition.category)
        }

        if (definition.zan2d != 0) {
            writeByte(95)
            writeShort(definition.zan2d)
        }

        if (definition.noteLinkId != -1) {
            writeByte(97)
            writeShort(definition.noteLinkId)
        }

        if (definition.noteTemplateId != -1) {
            writeByte(98)
            writeShort(definition.noteTemplateId)
        }

        for (i in definition.countObj.indices) {
            writeByte(100 + i)
            writeShort(definition.countObj[i])
            writeShort(definition.countCo[i])
        }

        if (definition.resizeX != 128) {
            writeByte(110)
            writeShort(definition.resizeX)
        }

        if (definition.resizeY != 128) {
            writeByte(111)
            writeShort(definition.resizeY)
        }

        if (definition.resizeZ != 128) {
            writeByte(112)
            writeShort(definition.resizeZ)
        }

        if (definition.ambient != 0) {
            writeByte(113)
            writeByte(definition.ambient)
        }

        if (definition.contrast != 0) {
            writeByte(114)
            writeByte(definition.contrast)
        }

        if (definition.teamCape != 0) {
            writeByte(115)
            writeByte(definition.teamCape)
        }

        if (definition.unnotedId != -1) {
            writeByte(139)
            writeShort(definition.unnotedId)
        }

        if (definition.notedId != -1) {
            writeByte(140)
            writeShort(definition.notedId)
        }

        if (definition.placeholderLink != -1) {
            writeByte(148)
            writeShort(definition.placeholderLink )
        }

        if (definition.placeholderTemplate != -1) {
            writeByte(149)
            writeShort(definition.placeholderTemplate)
        }

        definition.writeParameters(this)

        writeByte(0)
    }

}