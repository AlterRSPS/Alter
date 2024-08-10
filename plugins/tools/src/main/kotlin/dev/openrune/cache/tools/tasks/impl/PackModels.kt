package dev.openrune.cache.tools.tasks.impl

import com.displee.cache.CacheLibrary
import dev.openrune.cache.MODELS
import dev.openrune.cache.tools.tasks.CacheTask
import dev.openrune.cache.util.decompressGzipToBytes
import dev.openrune.cache.util.getFiles
import dev.openrune.cache.util.progress
import java.io.File
import java.nio.file.Files


class PackModels(private val modelDirectory : File) : CacheTask() {
    override fun init(library: CacheLibrary) {
        val modelSize = getFiles(modelDirectory,"gz","dat").size
        val progressModels = progress("Packing Models", modelSize)
        if (modelSize != 0) {
            getFiles(modelDirectory,"gz","dat").forEach {
                val id = it.nameWithoutExtension.toInt()
                val buffer = if (it.extension == "gz") decompressGzipToBytes(it.toPath()) else Files.readAllBytes(it.toPath())

                library.put(MODELS, id, buffer)

                progressModels.step()
            }
            library.index(7).update()
            progressModels.close()
        }
    }

}