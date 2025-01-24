package dev.openrune.cache.filestore.definition.encoder

import dev.openrune.cache.filestore.buffer.Writer
import dev.openrune.cache.filestore.definition.ConfigEncoder
import dev.openrune.cache.filestore.definition.data.HealthBarType
import dev.openrune.cache.filestore.definition.data.HitSplatType

class HealthBarEncoder : ConfigEncoder<HealthBarType>() {

    override fun Writer.encode(definition: HealthBarType) {
        if (definition.int1 != 255) {
            writeByte(2)
            writeByte(definition.int1)
        }
        if (definition.int2 != 255) {
            writeByte(3)
            writeByte(definition.int2)
        }
        if (definition.int3 != -1) {
            writeByte(4)
        }
        if (definition.int4 != 70) {
            writeByte(5)
            writeShort(definition.int4)
        }

        if (definition.frontSpriteId != -1) {
            writeByte(7)
            writeShort(definition.frontSpriteId)
        }
        if (definition.backSpriteId != -1) {
            writeByte(8)
            writeShort(definition.backSpriteId)
        }
        if (definition.int3 != -1) {
            writeByte(11)
            writeShort(definition.int3)
        }
        if (definition.width != 30) {
            writeByte(14)
            writeByte(definition.width)
        }

        if (definition.widthPadding != 0) {
            writeByte(15)
            writeByte(definition.widthPadding)
        }

        writeByte(0)
    }

}