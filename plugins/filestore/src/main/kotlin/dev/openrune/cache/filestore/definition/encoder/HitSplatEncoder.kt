package dev.openrune.cache.filestore.definition.encoder

import dev.openrune.cache.filestore.buffer.Writer
import dev.openrune.cache.filestore.definition.ConfigEncoder
import dev.openrune.cache.filestore.definition.data.HitSplatType

class HitSplatEncoder : ConfigEncoder<HitSplatType>() {

    override fun Writer.encode(definition: HitSplatType) {
        if (definition.font != -1) {
            writeByte(1)
            writeShort(definition.font)
        }
        if (definition.textColour != 16777215) {
            writeByte(2)
            writeMedium(definition.textColour)
        }
        if (definition.icon != -1) {
            writeByte(3)
            writeShort(definition.icon)
        }
        if (definition.left != -1) {
            writeByte(4)
            writeShort(definition.left)
        }
        if (definition.middle != -1) {
            writeByte(5)
            writeShort(definition.middle)
        }
        if (definition.right != -1) {
            writeByte(6)
            writeShort(definition.right)
        }
        if (definition.offsetX != 0) {
            writeByte(7)
            writeShort(definition.offsetX)
        }
        if (definition.amount != "") {
            writeByte(8)
            writePrefixedString(definition.amount)
        }
        if (definition.duration != 70) {
            writeByte(9)
            writeShort(definition.duration)
        }
        if (definition.offsetY != 0) {
            writeByte(10)
            writeShort(definition.offsetY)
        }

        if (definition.fade != -1) {
            writeByte(11)
        }

        if (definition.comparisonType != -1) {
            writeByte(12)
            writeByte(definition.comparisonType)
        }

        if (definition.damageYOfset != 0) {
            writeByte(13)
            writeShort(definition.damageYOfset)
        }

        if (definition.fade != 0) {
            writeByte(14)
            writeShort(definition.fade)
        }

        definition.writeTransforms(this, 17, 18)

        writeByte(0)
    }

}