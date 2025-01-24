package dev.openrune.cache.filestore.definition

import dev.openrune.cache.CacheManager.cache
import dev.openrune.cache.filestore.Cache
import dev.openrune.cache.filestore.buffer.BufferReader
import dev.openrune.cache.filestore.buffer.Reader
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.nio.BufferUnderflowException

abstract class DefinitionDecoder<T : Definition>(val index: Int) {

    open fun files() = cache.files(2,getArchive(0))

    fun createMap(factory: (Int) -> T): Int2ObjectOpenHashMap<T> =
        Int2ObjectOpenHashMap<T>().apply {
            files().forEach { put(it, factory(it)) }}

    abstract fun create(): Int2ObjectOpenHashMap<T>

    open fun load(definitions: Int2ObjectOpenHashMap<T>, reader: Reader) {
        val id = readId(reader)
        read(definitions, id, reader)
    }

    open fun readId(reader: Reader) = reader.readInt()

    /**
     * Load from cache
     */
    open fun load(cache: Cache): Int2ObjectOpenHashMap<T> {
        val start = System.currentTimeMillis()
        val definitions = create()
        definitions.forEach {
            try {
                load(definitions, cache, it.key)
            } catch (e: BufferUnderflowException) {
                logger.error(e) { "Error reading definition ${it.key}" }
                throw e
            }
        }
        logger.info { "${definitions.size} ${this::class.simpleName} definitions loaded in ${System.currentTimeMillis() - start}ms" }
        return definitions
    }

    open fun load(definitions: Int2ObjectOpenHashMap<T>, cache: Cache, id: Int) {
        val archive = getArchive(id)
        val file = getFile(id)
        val data = cache.data(index, archive, file) ?: return
        read(definitions, id, BufferReader(data))
    }

    protected fun readFlat(definitions: Int2ObjectOpenHashMap<T>, id: Int, reader: Reader) {
        val definition = definitions[id]
        readFlatFile(definition, reader)
        changeValues(definitions, definition)
    }

    open fun readFlatFile(definition: T, buffer: Reader) {
        definition.read(0,buffer)
    }

    open fun loadSingle(id: Int, data: ByteArray): T? {
        val definitions = Int2ObjectOpenHashMap<T>(1)
        val reader = BufferReader(data)
        read(definitions, 0, reader)
        return definitions[0]
    }

    open fun loadSingleFlat(id: Int, data: ByteArray): T? {
        val definitions = Int2ObjectOpenHashMap<T>(1)
        val reader = BufferReader(data)
        readFlat(definitions, 0, reader)
        return definitions[0]
    }


    open fun getFile(id: Int) = id

    open fun getArchive(id: Int) = id

    protected fun read(definitions: Int2ObjectOpenHashMap<T>, id: Int, reader: Reader) {
        val definition = definitions[id]
        readLoop(definition, reader)
        changeValues(definitions, definition)
    }

    open fun readLoop(definition: T, buffer: Reader) {
        while (true) {
            val opcode = buffer.readUnsignedByte()
            if (opcode == 0) {
                break
            }
            definition.read(opcode, buffer)
        }
    }

    protected abstract fun T.read(opcode: Int, buffer: Reader)

    open fun changeValues(definitions: Int2ObjectOpenHashMap<T>, definition: T) {
    }

    companion object {
        internal val logger = KotlinLogging.logger {}
    }
}
