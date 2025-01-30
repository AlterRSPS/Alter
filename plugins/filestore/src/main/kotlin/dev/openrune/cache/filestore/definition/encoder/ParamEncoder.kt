package dev.openrune.cache.filestore.definition.encoder

import dev.openrune.cache.filestore.buffer.Writer
import dev.openrune.cache.filestore.definition.ConfigEncoder
import dev.openrune.cache.filestore.definition.data.ParamType
import dev.openrune.cache.filestore.definition.data.StructType

class ParamEncoder: ConfigEncoder<ParamType>() {
    override fun Writer.encode(definition: ParamType) {
        if(definition.type != null) {
            writeByte(1)
            writeByte(definition.type!!.keyChar.code)
        }
        if(definition.defaultInt != 0) {
            writeByte(3)
            writeInt(definition.defaultInt)
        }
        if(definition.isMembers) {
            writeByte(4)
        }
        if(definition.defaultString != null) {
            writeByte(5)
            writeString(definition.defaultString)
        }
        writeByte(0)
    }
}