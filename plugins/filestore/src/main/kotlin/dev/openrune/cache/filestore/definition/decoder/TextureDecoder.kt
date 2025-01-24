package dev.openrune.cache.filestore.definition.decoder

import dev.openrune.cache.CacheManager
import dev.openrune.cache.STRUCT
import dev.openrune.cache.TEXTURES
import dev.openrune.cache.filestore.Cache
import dev.openrune.cache.filestore.buffer.BufferReader
import dev.openrune.cache.filestore.definition.DefinitionDecoder
import dev.openrune.cache.filestore.buffer.Reader
import dev.openrune.cache.filestore.definition.data.StructType
import dev.openrune.cache.filestore.definition.data.TextureType
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

class TextureDecoder : DefinitionDecoder<TextureType>(TEXTURES) {


    override fun create(): Int2ObjectOpenHashMap<TextureType> = createMap { TextureType(it) }

    override fun getArchive(id: Int) = 0

    override fun getFile(id: Int) = 0

    override fun load(cache: Cache): Int2ObjectOpenHashMap<TextureType> {
        val fileList = CacheManager.cache.files(index, 0)
        val size = fileList.size
        val map = Int2ObjectOpenHashMap<TextureType>()
        val start = System.currentTimeMillis()

        fileList.forEach { fileId ->
            CacheManager.cache.data(TEXTURES, 0, fileId)?.let { data ->
                val reader = BufferReader(data)
                val type = TextureType().apply {
                    read(0, reader)
                }
                map[fileId] = type
            }
        }

        logger.info { "$size ${this::class.simpleName} definitions loaded in ${System.currentTimeMillis() - start}ms" }
        return map
    }

    override fun TextureType.read(opcode: Int, buffer: Reader) {
        averageRgb = buffer.readUnsignedShort()
        isTransparent = buffer.readUnsignedByte() == 1
        val count: Int = buffer.readUnsignedByte()

        if (count in 1..4) {
            fileIds = IntArray(count).toMutableList()
            for (index in 0 until count) {
                fileIds[index] = buffer.readUnsignedShort()
            }

            if (count > 1) {

                combineModes = IntArray(count -1).toMutableList()
                for (index in 0 until count - 1) {
                    combineModes[index] = buffer.readUnsignedShort()
                }

                field2440 = IntArray(count -1).toMutableList()
                for (index in 0 until count - 1) {
                    field2440[index] = buffer.readUnsignedShort()
                }

            }

            colourAdjustments = IntArray(count).toMutableList()
            for (index in 0 until count) {
                colourAdjustments[index] = buffer.readInt()
            }

            animationDirection = buffer.readUnsignedByte()
            animationSpeed = buffer.readUnsignedByte()
        }
    }
}