package dev.openrune.cache.filestore.definition

import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.buffer.Writer

interface Recolourable {
    var originalColours: MutableList<Int>?
    var modifiedColours: MutableList<Int>?
    var originalTextureColours: MutableList<Int>?
    var modifiedTextureColours: MutableList<Int>?

    fun readColours(buffer: Reader) {
        val length = buffer.readUnsignedByte()
        originalColours = MutableList(length) { -1 }
        modifiedColours = MutableList(length) { -1 }
        for (count in 0 until length) {
            originalColours!![count] = buffer.readShort().toShort().toInt()
            modifiedColours!![count] = buffer.readShort().toShort().toInt()
        }
    }

    fun readTextures(buffer: Reader) {
        val length = buffer.readUnsignedByte()
        originalTextureColours = MutableList(length) { -1 }
        modifiedTextureColours = MutableList(length) { -1 }
        for (count in 0 until length) {
            originalTextureColours!![count] = buffer.readShort().toShort().toInt()
            modifiedTextureColours!![count] = buffer.readShort().toShort().toInt()
        }
    }

    fun writeColoursTextures(writer: Writer) {
        writeArray(writer, 40, originalColours, modifiedColours)
        writeArray(writer, 41, originalTextureColours, modifiedTextureColours)
    }

    private fun writeArray(writer: Writer, opcode: Int, original: List<Int>?, modified: List<Int>?) {
        if (original != null && modified != null) {
            writer.writeByte(opcode)
            writer.writeByte(original.size)
            for (i in original.indices) {
                writer.writeShort(original[i])
                writer.writeShort(modified[i])
            }
        }
    }

}