package dev.openrune.cache.filestore.definition.decoder

import dev.openrune.cache.CONFIGS
import dev.openrune.cache.DBROW
import dev.openrune.cache.VARBIT
import dev.openrune.cache.VARPLAYER
import dev.openrune.cache.filestore.definition.DefinitionDecoder
import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.definition.data.VarBitType
import dev.openrune.cache.filestore.definition.data.VarpType
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

class VarDecoder : DefinitionDecoder<VarpType>(CONFIGS) {

    override fun getArchive(id: Int) = VARPLAYER

    override fun create(): Int2ObjectOpenHashMap<VarpType> = createMap { VarpType(it) }

    override fun getFile(id: Int) = id

    override fun VarpType.read(opcode: Int, buffer: Reader) {
        if (opcode == 5) {
            configType = buffer.readUnsignedShort()
        }
    }
}