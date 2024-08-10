package dev.openrune.cache.filestore.definition.decoder

import dev.openrune.cache.TEXTURES
import dev.openrune.cache.filestore.definition.DefinitionDecoder
import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.definition.data.TextureType

class TextureDecoder : DefinitionDecoder<TextureType>(TEXTURES) {

    override fun create(size: Int) = Array(size) { TextureType(it) }

    override fun getFile(id: Int) = id

    override fun TextureType.read(opcode: Int, buffer: Reader) {
        averageRgb = buffer.readUnsignedShort()
        isTransparent = buffer.readUnsignedByte() == 1
        val count: Int = buffer.readUnsignedByte()

        if (count in 1..4) {
            fileIds = IntArray(count).toMutableList()
            for (index in 0 until count) {
                fileIds[index] = buffer.readUnsignedShort()
            }

            if (count > 1) {

                combineModes = IntArray(count -1).toMutableList()
                for (index in 0 until count - 1) {
                    combineModes[index] = buffer.readUnsignedShort()
                }

                field2440 = IntArray(count -1).toMutableList()
                for (index in 0 until count - 1) {
                    field2440[index] = buffer.readUnsignedShort()
                }

            }

            colourAdjustments = IntArray(count).toMutableList()
            for (index in 0 until count) {
                colourAdjustments[index] = buffer.readInt()
            }

            animationDirection = buffer.readUnsignedByte()
            animationSpeed = buffer.readUnsignedByte()
        }
    }
}