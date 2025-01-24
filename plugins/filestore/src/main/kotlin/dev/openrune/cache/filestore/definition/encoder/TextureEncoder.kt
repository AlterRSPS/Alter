package dev.openrune.cache.filestore.definition.encoder

import dev.openrune.cache.filestore.buffer.Writer
import dev.openrune.cache.filestore.definition.ConfigEncoder
import dev.openrune.cache.filestore.definition.data.TextureType

class TextureEncoder: ConfigEncoder<TextureType>() {

    override fun Writer.encode(definition: TextureType) {
        writeShort(definition.averageRgb)
        writeByte(if(definition.isTransparent) 1 else 0)
        val fileCount = definition.fileIds.size
        writeByte(fileCount)
        for(index in 0..<fileCount) {
            writeShort(definition.fileIds[index])
        }
        if (fileCount > 1) {
            definition.combineModes.forEach { combineMode ->
                writeByte(combineMode)
            }

            definition.field2440.forEach { field2440 ->
                writeByte(field2440)
            }

        }

        definition.colourAdjustments.forEach { colourAdjustment ->
            writeInt(colourAdjustment)
        }

        writeByte(definition.animationDirection)
        writeByte(definition.animationSpeed)
    }
}