package dev.openrune.cache.tools.tasks.impl

import com.displee.cache.CacheLibrary
import dev.openrune.RSCMHandler
import dev.openrune.cache.MODELS
import dev.openrune.cache.tools.tasks.CacheTask
import dev.openrune.cache.util.decompressGzipToBytes
import dev.openrune.cache.util.getFiles
import dev.openrune.cache.util.progress
import java.io.File
import java.nio.file.Files


class PackModels(
    private val modelDirectory: File,
    private val rscmMappingPrefix: String = "models."
) : CacheTask() {
    override fun init(library: CacheLibrary) {
        val modelFiles = getFiles(modelDirectory, "gz", "dat")
        val modelSize = modelFiles.size
        val progressModels = progress("Packing Models", modelSize)

        if (modelSize > 0) {
            modelFiles.forEach { file ->
                val name = file.nameWithoutExtension
                val id: Int? = if (name.matches(Regex("-?\\d+"))) {
                    name.toInt()
                } else {
                    RSCMHandler.getMapping(rscmMappingPrefix + name.lowercase().replace(" ", "_"))
                }

                val buffer = if (file.extension == "gz") {
                    decompressGzipToBytes(file.toPath())
                } else {
                    Files.readAllBytes(file.toPath())
                }

                if (id != null) {
                    library.put(MODELS, id, buffer)
                } else {
                    println("Unable to pack model")
                }

                progressModels.step()
            }

            library.index(7).update()
            progressModels.close()
        }
    }
}