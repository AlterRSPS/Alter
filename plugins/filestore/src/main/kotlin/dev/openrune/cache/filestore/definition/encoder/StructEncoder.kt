package dev.openrune.cache.filestore.definition.encoder

import dev.openrune.cache.filestore.buffer.Writer
import dev.openrune.cache.filestore.definition.ConfigEncoder
import dev.openrune.cache.filestore.definition.data.StructType

class StructEncoder: ConfigEncoder<StructType>() {
    override fun Writer.encode(definition: StructType) {
        definition.writeParameters(this)

        writeByte(0)
    }
}