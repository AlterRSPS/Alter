package org.alter.game.rsprot

import dev.openrune.cache.CacheManager
import dev.openrune.cache.filestore.Cache
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.util.ReferenceCounted
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.rsprot.protocol.api.js5.Js5GroupProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.min
import kotlin.math.pow

class CacheJs5GroupProvider : Js5GroupProvider  {
    override fun provide(
        archive: Int,
        group: Int,
    ): ByteBuf? {
        return groups[bitpack(archive, group)]
    }

    private val groups: Int2ObjectMap<ByteBuf> = Int2ObjectOpenHashMap(2.toDouble().pow(17).toInt())

    fun load() {
        val cache = CacheManager.cache

        encodeMasterIndex(cache) //TODO set to versiontable from filestore lib

        for (index in cache.indices()) {
            encodeArchive(cache, index)
        }
        encodeArchiveMasterIndex(cache)

        logger.info("Loaded {} JS5 responses", groups.size)

    }

    private fun encodeMasterIndex(cache: Cache) {
        Unpooled.directBuffer().use { uncompressed ->
            uncompressed.writeBytes(cache.versionTable)
            encodeGroup(255, 255, uncompressed)
        }
    }

    private fun encodeArchiveMasterIndex(
        cache: Cache,
    ) {
        for (archive in cache.indices()) {
            if (archive == 255) continue // this is the prebuilt versiontable.
            val data = cache.sector(255, archive) ?: continue

            Unpooled.directBuffer().use { uncompressed ->
                uncompressed.writeBytes(data)
                encodeGroup(255, archive, uncompressed)
            }
        }
    }

    private fun encodeArchive(
        cache: Cache,
        index: Int,
    ) {
        for (archive in cache.archives(index)) {
            val data = cache.sector(index, archive) ?: continue

            Unpooled.directBuffer().use { uncompressed ->
                uncompressed.writeBytes(data)
                strip(uncompressed)
                encodeGroup(index, archive, uncompressed)
            }
        }
    }

    private fun encodeGroup(
        archive: Int,
        group: Int,
        data: ByteBuf,
    ) {
        val response =
            Unpooled.directBuffer()
                .writeByte(archive)
                .writeShort(group)
                .writeByte(data.readUnsignedByte().toInt()) // compression
                .writeBytes(data, min(data.readableBytes(), BYTES_BEFORE_BLOCK))
        while (data.isReadable) {
            response.writeByte(0xFF)
            response.writeBytes(data, min(data.readableBytes(), BYTES_AFTER_BLOCK))
        }

        val bitpack = bitpack(archive, group)
        groups[bitpack] = Unpooled.unreleasableBuffer(response)
    }

    private fun strip(buf: ByteBuf): Int? {
        return if (buf.readableBytes() >= 2) {
            val index = buf.writerIndex() - 2
            val version = buf.getUnsignedShort(index)
            buf.writerIndex(index)
            version
        } else {
            null
        }
    }

    private inline fun <T : ReferenceCounted?, R> T.use(block: (T) -> R): R {
        try {
            return block(this)
        } finally {
            this?.release()
        }
    }

    private companion object {
        private const val BLOCK_SIZE = 512
        private const val BLOCK_HEADER_SIZE = 1 + 2 + 1
        private const val BLOCK_DELIMITER_SIZE = 1
        private const val BYTES_BEFORE_BLOCK = BLOCK_SIZE - BLOCK_HEADER_SIZE
        private const val BYTES_AFTER_BLOCK = BLOCK_SIZE - BLOCK_DELIMITER_SIZE

        private val logger: Logger = LoggerFactory.getLogger(CacheJs5GroupProvider::class.java)

        fun bitpack(
            archive: Int,
            group: Int,
        ): Int {
            require(archive and 0xFF.inv() == 0) { "invalid archive $archive:$group" }
            require(group and 0xFFFF.inv() == 0) { "invalid group $archive:$group" }

            return ((archive and 0xFF) shl 16) or (group and 0xFFFF)
        }
    }
}
