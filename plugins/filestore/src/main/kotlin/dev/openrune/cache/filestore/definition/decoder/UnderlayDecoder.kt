package dev.openrune.cache.filestore.definition.decoder

import dev.openrune.cache.*
import dev.openrune.cache.filestore.definition.DefinitionDecoder
import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.definition.data.StructType
import dev.openrune.cache.filestore.definition.data.UnderlayType
import dev.openrune.cache.filestore.definition.data.VarpType
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

class UnderlayDecoder : DefinitionDecoder<UnderlayType>(CONFIGS) {

    override fun getArchive(id: Int) = UNDERLAY

    override fun create(): Int2ObjectOpenHashMap<UnderlayType> = createMap { UnderlayType(it) }

    override fun getFile(id: Int) = id

    override fun UnderlayType.read(opcode: Int, buffer: Reader) {
        if (opcode == 1) {
            color = buffer.readMedium()
        }
    }
}