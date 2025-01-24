package dev.openrune.cache.filestore.definition.decoder

import dev.openrune.cache.CONFIGS
import dev.openrune.cache.DBROW
import dev.openrune.cache.SPRITES
import dev.openrune.cache.STRUCT
import dev.openrune.cache.filestore.definition.DefinitionDecoder
import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.definition.data.SpriteType
import dev.openrune.cache.filestore.definition.data.StructType
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

class StructDecoder : DefinitionDecoder<StructType>(CONFIGS) {

    override fun getArchive(id: Int) = STRUCT

    override fun create(): Int2ObjectOpenHashMap<StructType> = createMap { StructType(it) }

    override fun getFile(id: Int) = id

    override fun StructType.read(opcode: Int, buffer: Reader) {
        if (opcode == 249) {
            readParameters(buffer)
        }
    }
}