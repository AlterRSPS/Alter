package dev.openrune.cache.filestore.definition.encoder

import dev.openrune.cache.filestore.buffer.Writer
import dev.openrune.cache.filestore.definition.ConfigEncoder
import dev.openrune.cache.filestore.definition.data.DBTableType
import dev.openrune.cache.filestore.definition.data.TextureType
import dev.openrune.cache.util.ScriptVarType

class DBTableEncoder: ConfigEncoder<DBTableType>() {

    override fun Writer.encode(definition: DBTableType) {
        val types = definition.types
        val defaultValues: Array<Array<Any?>?>? = definition.defaultColumnValues
        if (types == null) {
            writeByte(0)
            return
        }
        writeByte(1)
        writeByte(types.size)
        types.indices.forEach { i ->
            val columnTypes = types[i] ?: return@forEach
            val hasDefault = defaultValues != null && defaultValues[i] != null
            var setting = i
            if (hasDefault) {
                setting = setting or 0x80
            }
            writeByte(setting)
            writeByte(columnTypes.size)
            for (type in columnTypes) {
                writeSmart(type.id)
            }
            if (hasDefault) {
                writeColumnFields(columnTypes, defaultValues!![i])
            }
        }
        writeByte(255)
        writeByte(0)
    }
}

fun Writer.writeColumnFields(types: Array<ScriptVarType>, values: Array<Any?>?) {
    val fieldCount = values!!.size / types.size
    writeSmart(fieldCount)
    for (fieldIndex in 0 until fieldCount) {
        for (typeIndex in types.indices) {
            val type = types[typeIndex]
            val valuesIndex = fieldIndex * types.size + typeIndex
            if (type == ScriptVarType.STRING) {
                writeString(values[valuesIndex] as String?)
            } else {
                writeInt((values[valuesIndex] as Int?)!!)
            }
        }
    }
}