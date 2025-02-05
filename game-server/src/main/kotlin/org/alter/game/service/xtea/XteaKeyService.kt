package org.alter.game.service.xtea

import com.google.gson.Gson
import dev.openrune.cache.CacheManager
import dev.openrune.cache.MAPS
import gg.rsmod.util.ServerProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.rsprot.crypto.xtea.XteaKey
import net.rsprot.protocol.game.outgoing.map.util.XteaProvider
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.service.Service
import org.apache.commons.io.FilenameUtils
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * A [Service] that loads and exposes XTEA keys required for map decryption.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class XteaKeyService : Service, XteaProvider {
    private val keys = Int2ObjectOpenHashMap<IntArray>()

    val validRegions: IntArray
        get() = keys.keys.toIntArray()

    override fun init(
        server: Server,
        world: World,
        serviceProperties: ServerProperties,
    ) {
        val path = Paths.get(serviceProperties.getOrDefault("path", "../data/"))
        val singleFile = path.resolve("xteas.json")
        if (Files.exists(singleFile)) {
            loadSingleFile(singleFile)
        } else {
            throw FileNotFoundException(
                "Missing xteas.json file at $path. NOTE: You get it in same zip file from which you extracted the cache.",
            )
        }

        loadKeys(world)
    }

    fun get(region: Int): IntArray {
        if (keys[region] == null) {
            logger.trace { "No XTEA keys found for region $region." }
            keys[region] = EMPTY_KEYS
        }
        return keys[region]!!
    }

    private fun loadKeys(world: World) {
        /*
         * Get the total amount of valid regions and which keys we are missing.
         */
        val maxRegions = Short.MAX_VALUE
        var totalRegions = 0
        val missingKeys = mutableListOf<Int>()

        val cache = CacheManager.cache
        for (regionId in 0 until maxRegions) {
            val x = regionId shr 8
            val z = regionId and 0xFF

            /*
             * Check if the region corresponding to the x and z can be
             * found in our cache.
             */
            cache.data(MAPS, "m${x}_$z") ?: continue

            /*
             * The region was found in the regionIndex.
             */
            totalRegions++

            /*
             * If the XTEA is not found in our xteaService, we know the keys
             * are missing.
             */
            if (get(regionId).contentEquals(EMPTY_KEYS)) {
                missingKeys.add(regionId)
            }
        }

        /*
         * Set the XTEA service for the [World].
         */
        world.xteaKeyService = this
    }

    private fun loadSingleFile(path: Path) {
        val reader = Files.newBufferedReader(path)
        val xteas = Gson().fromJson(reader, Array<XteaFile>::class.java)
        reader.close()
        xteas?.forEach { xtea ->
            keys[xtea.mapsquare] = xtea.key
        }
    }

    private fun loadDirectory(path: Path) {
        Files.list(path).forEach { list ->
            val region = FilenameUtils.removeExtension(list.fileName.toString()).toInt()
            val keys = IntArray(4)
            Files.newBufferedReader(list).useLines { lines ->
                lines.forEachIndexed { index, line ->
                    val key = line.toInt()
                    keys[index] = key
                }
            }
            this.keys[region] = keys
        }
    }

    private data class XteaFile(val mapsquare: Int, val key: IntArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as XteaFile

            if (mapsquare != other.mapsquare) return false
            if (!key.contentEquals(other.key)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = mapsquare
            result = 31 * result + key.contentHashCode()
            return result
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
        val EMPTY_KEYS = intArrayOf(0, 0, 0, 0)
    }

    override fun provide(region: Int): XteaKey {
        if (keys[region] == null) {
            logger.trace { "No XTEA keys found for region $region." }
            keys[region] = EMPTY_KEYS
        }
        return XteaKey(keys[region]!!)
    }
}
