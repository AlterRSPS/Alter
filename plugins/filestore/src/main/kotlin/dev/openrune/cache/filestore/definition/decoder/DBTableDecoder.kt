package dev.openrune.cache.filestore.definition.decoder

import dev.openrune.cache.CONFIGS
import dev.openrune.cache.DBTABLE
import dev.openrune.cache.filestore.definition.DefinitionDecoder
import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.definition.data.DBTableType
import dev.openrune.cache.util.ScriptVarType
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

class DBTableDecoder : DefinitionDecoder<DBTableType>(CONFIGS) {

    override fun getArchive(id: Int) = DBTABLE

    override fun create(): Int2ObjectOpenHashMap<DBTableType> = createMap { DBTableType(it) }

    override fun getFile(id: Int) = id

    override fun DBTableType.read(opcode: Int, buffer: Reader) {
        when (opcode) {
            1 -> {
                val numColumns = buffer.readUnsignedByte()
                val types = arrayOfNulls<Array<ScriptVarType>>(numColumns)
                var defaultValues: Array<Array<Any?>?>? = null
                while (true) {
                    var setting = buffer.readUnsignedByte().toInt()
                    if (setting == 0xFF) break
                    val columnId = setting and 0x7F
                    val hasDefault = setting and 0x80 != 0

                    val columnTypes = Array(buffer.readUnsignedByte()) {
                        try {
                            ScriptVarType.forId((buffer.readSmart()))!!
                        } catch (e: Exception) {
                            logger.error(e) { "Unable to decode var type for db table $id" }
                            throw e
                        }
                    }
                    types[columnId] = columnTypes
                    if (hasDefault) {
                        if (defaultValues == null) {
                            defaultValues = arrayOfNulls<Array<Any?>?>(types.size)
                        }
                        defaultValues[columnId] = decodeColumnFields(buffer, columnTypes)
                    }
                }
                this.types = types
                this.defaultColumnValues = defaultValues
            }
            else -> error("Unknown opcode: $opcode")
        }
    }
}

fun decodeColumnFields(buffer: Reader, types: Array<ScriptVarType>): Array<Any?> {
    val fieldCount = buffer.readSmart()
    val values = arrayOfNulls<Any>(fieldCount * types.size)
    for (fieldIndex in 0 until fieldCount) {
        for (typeIndex in types.indices) {
            val type = types[typeIndex]
            val valuesIndex = fieldIndex * types.size + typeIndex
            values[valuesIndex] = when (type) {
                ScriptVarType.STRING -> buffer.readString()
                else -> buffer.readInt()
            }
        }
    }
    return values
}