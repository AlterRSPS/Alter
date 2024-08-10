package dev.openrune.cache.filestore.definition.encoder

import dev.openrune.cache.filestore.buffer.Writer
import dev.openrune.cache.filestore.definition.ConfigEncoder
import dev.openrune.cache.filestore.definition.data.AreaType
import dev.openrune.cache.filestore.definition.data.HitSplatType

class AreaEncoder : ConfigEncoder<AreaType>() {

    override fun Writer.encode(definition: AreaType) {

        if (definition.sprite1 != -1) {
            writeByte(1)
            writeSmart(definition.sprite1)
        }

        if (definition.sprite2 != -1) {
            writeByte(2)
            writeSmart(definition.sprite2)
        }

        if (definition.name != "null") {
            writeByte(3)
            writeString(definition.name)
        }

        if (definition.fontColor != 0) {
            writeByte(4)
            writeMedium(definition.fontColor)
        }

        if (definition.textSize != 0) {
            writeByte(6)
            writeMedium(definition.textSize)
        }

        if (definition.options.any { it != null }) {
            for (i in definition.options.indices) {
                writeByte(7 + i)
                System.out.println("Op : ${7 + i}")
                if (definition.options[i] == null) {
                    continue
                }
                writeString(definition.options[i]!!)
            }
        }

        if (definition.menuTargetName != "null") {
            writeByte(17)
            writeString(definition.menuTargetName)
        }
        if (definition.category != 0) {
            writeByte(19)
            writeShort(definition.category)
        }

        if (definition.horizontalAlignment != 1) {
            writeByte(29)
            writeByte(definition.horizontalAlignment)
        }

        if (definition.verticalAlignment != 1) {
            writeByte(30)
            writeByte(definition.verticalAlignment)
        }

        writeByte(0)
    }

}