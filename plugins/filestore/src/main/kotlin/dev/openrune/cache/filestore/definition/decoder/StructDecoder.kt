package dev.openrune.cache.filestore.definition.decoder

import dev.openrune.cache.STRUCT
import dev.openrune.cache.filestore.definition.DefinitionDecoder
import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.definition.data.StructType

class StructDecoder : DefinitionDecoder<StructType>(STRUCT) {

    override fun create(size: Int) = Array(size) { StructType(it) }

    override fun getFile(id: Int) = id

    override fun StructType.read(opcode: Int, buffer: Reader) {
        if (opcode == 249) {
            readParameters(buffer)
        }
    }
}