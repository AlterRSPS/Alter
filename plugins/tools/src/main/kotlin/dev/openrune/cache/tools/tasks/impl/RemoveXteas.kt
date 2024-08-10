package dev.openrune.cache.tools.tasks.impl

import com.displee.cache.CacheLibrary
import dev.openrune.cache.MAPS
import dev.openrune.cache.util.XteaLoader
import dev.openrune.cache.tools.tasks.CacheTask
import dev.openrune.cache.util.FileUtil
import dev.openrune.cache.util.progress
import java.io.File

/*
 * Removes Xteas Encryption from the maps
 */
class RemoveXteas(private val xteaLocation : File = File(FileUtil.getTemp(), "xteas.json")) : CacheTask() {
    override fun init(library: CacheLibrary) {
        XteaLoader.load(xteaLocation)
        val index = library.index(5)
        var mapCount = 0
        for (x in 0..256) {
            for (y in 0..256) {
                val landscapeId = index.archiveId("l${x}_${y}")
                if (landscapeId != -1) {
                    mapCount++
                }
            }
        }
        val mapProgress = progress("Removing Xteas Maps", mapCount)

        for (x in 0..256) {
            for (y in 0..256) {
                val regionId = x shl 8 or y
                val landscapeId = index.archiveId("l${x}_${y}")
                if (landscapeId != -1) {
                    val keys = XteaLoader.getKeys(regionId)
                    val landscape = library.data(5, "l${x}_${y}", keys)
                    if (landscape != null) {
                        library.put(MAPS, "l${x}_${y}", landscape)
                    }
                    mapProgress.step()
                }
            }
        }
        mapProgress.close()
    }
}