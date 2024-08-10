package dev.openrune.cache.filestore.definition

import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.buffer.Writer
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap

interface Parameterized {

    var params: Map<Int, Any>?

    fun readParameters(buffer: Reader) {
        val length = buffer.readUnsignedByte()
        if (length == 0) {
            return
        }
        val params = Int2ObjectArrayMap<Any>()
        for (i in 0 until length) {
            val string = buffer.readUnsignedBoolean()
            val id = buffer.readUnsignedMedium()
            params[id] = if (string) buffer.readString() else buffer.readInt()
        }
        this.params = params
    }

    fun writeParameters(writer: Writer) {
        params?.let { params ->
            writer.writeByte(249)
            writer.writeByte(params.size)
            params.forEach { (id, value) ->
                writer.writeByte(value is String)
                writer.writeMedium(id)
                if (value is String) {
                    writer.writeString(value)
                } else if (value is Int) {
                    writer.writeInt(value)
                }
            }
        }
    }

}