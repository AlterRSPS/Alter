package dev.openrune.cache.tools.tasks.impl

import com.displee.cache.CacheLibrary
import com.google.gson.GsonBuilder
import dev.openrune.cache.MAPS
import dev.openrune.cache.util.XteaLoader
import dev.openrune.cache.util.logger
import dev.openrune.cache.tools.tasks.CacheTask
import dev.openrune.cache.util.decompressGzipToBytes
import dev.openrune.cache.util.getFiles
import dev.openrune.cache.util.progress
import java.io.File
import java.nio.file.Files
import kotlin.random.Random

enum class XteaType {
    NO_KEYS,
    EMPTY_KEYS,
    RANDOM_KEYS
}

class PackMaps(private val mapsDirectory: File, private val xteaLocation: File = File("xteas.json"), private val xteaType: XteaType = XteaType.NO_KEYS) : CacheTask() {
    override fun init(library: CacheLibrary) {
        val encodeXteas = xteaType != XteaType.NO_KEYS

        if (encodeXteas && !xteaLocation.exists()) {
            logger.info { "Unable to find Xteas File at $xteaLocation" }
            return
        } else if (encodeXteas) {
            XteaLoader.load(xteaLocation)
        }

        val mapSize = getFiles(mapsDirectory,"gz","dat").size
        val progressMaps = progress("Packing Maps", mapSize * 1)
        if (mapSize != 0) {
            getFiles(mapsDirectory,"gz","dat").filter { it.name.startsWith("l") }.forEach { mapFile ->
                if (mapFile.name.first().toString() == "l") {

                    val objectFile = File(mapFile.parent,mapFile.name.replaceFirstChar { "m" })

                    if (objectFile.exists()) {

                        val loc = mapFile.nameWithoutExtension.replace(
                            "m",""
                        ).replace("l","").split("_")

                        val regionId = loc[0].toInt() shl 8 or loc[1].toInt()

                        var tileData = Files.readAllBytes(mapFile.toPath())
                        var objData = Files.readAllBytes(objectFile.toPath())

                        if (mapFile.name.endsWith(".gz")) {
                            tileData = decompressGzipToBytes(mapFile.toPath())
                        }
                        if (objectFile.name.endsWith(".gz")) {
                            objData = decompressGzipToBytes(objectFile.toPath())
                        }

                        val keys : IntArray? = when(xteaType) {
                            XteaType.NO_KEYS -> null
                            XteaType.EMPTY_KEYS -> intArrayOf(0,0,0,0)
                            XteaType.RANDOM_KEYS -> generateRandomIntArray()
                        }

                        if (keys != null) {
                            XteaLoader.xteas[regionId]!!.key = keys
                        }

                        packMap(library,loc[0].toInt(), loc[1].toInt(), tileData, objData,keys)
                    } else {
                        println("MISSING MAP FILE: $objectFile")
                    }

                }

                progressMaps.stepBy(3)

            }

            if (encodeXteas) {
                val valuesList = XteaLoader.xteas.values.toList()
                xteaLocation.writeText(GsonBuilder().setPrettyPrinting().create().toJson(valuesList))
            }
            progressMaps.close()
        }
    }

    fun packMap(library: CacheLibrary, regionX : Int, regionY : Int, tileData : ByteArray, objData : ByteArray, keys : IntArray?) {
        val mapArchiveName = "m" + regionX + "_" + regionY
        val landArchiveName = "l" + regionX + "_" + regionY

        library.put(MAPS, mapArchiveName, tileData)
        library.put(MAPS, landArchiveName, objData,keys)
    }

    fun generateRandomIntArray(): IntArray {
        return IntArray(4) { Random.nextInt(Int.MIN_VALUE, Int.MAX_VALUE) }
    }

}