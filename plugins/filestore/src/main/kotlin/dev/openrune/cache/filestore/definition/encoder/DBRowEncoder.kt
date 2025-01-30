package dev.openrune.cache.filestore.definition.encoder

import dev.openrune.cache.filestore.buffer.Writer
import dev.openrune.cache.filestore.definition.ConfigEncoder
import dev.openrune.cache.filestore.definition.data.DBRowType

class DBRowEncoder: ConfigEncoder<DBRowType>() {

    override fun Writer.encode(definition: DBRowType) {
        when {
            definition.columnTypes != null -> {
                writeByte(3)
                val types = definition.columnTypes
                val columnValues = definition.columnValues
                writeByte(types!!.size)
                types.indices.forEach { columnId ->
                    val columnTypes = types[columnId] ?: return@forEach
                    writeByte(columnId)
                    writeByte(columnTypes.size)
                    for (type in columnTypes) {
                        writeSmart(type.id)
                    }
                    writeColumnFields(columnTypes, columnValues!![columnId])
                }
                writeByte(255)
            }
        }
        if (definition.tableId != -1) {
            writeByte(4)
            writeVarInt(definition.tableId)
        }
        writeByte(0)
    }
}

fun Writer.writeVarInt(var1: Int) {
    if (var1 and -128 != 0) {
        if (var1 and -16384 != 0) {
            if (var1 and -2097152 != 0) {
                if (var1 and -268435456 != 0) {
                    writeByte(var1 ushr 28 or 128)
                }
                writeByte(var1 ushr 21 or 128)
            }
            writeByte(var1 ushr 14 or 128)
        }
        writeByte(var1 ushr 7 or 128)
    }
    writeByte(var1 and 127)
}