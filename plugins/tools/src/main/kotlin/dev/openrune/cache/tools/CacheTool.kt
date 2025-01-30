package dev.openrune.cache.tools

import com.displee.cache.CacheLibrary
import dev.openrune.cache.tools.CacheTool.Constants.builder
import dev.openrune.cache.tools.CacheTool.Constants.library
import dev.openrune.cache.tools.tasks.TaskType
import dev.openrune.cache.util.FileUtil
import dev.openrune.cache.util.FileUtil.cacheLocation
import io.github.oshai.kotlinlogging.KotlinLogging
import org.jire.js5server.Js5Server
import java.io.File
import kotlin.system.exitProcess

class CacheTool(configs: Builder) {

    object Constants {
        lateinit var builder : Builder
        lateinit var library: CacheLibrary
    }

    private val logger = KotlinLogging.logger {}

    init {
        builder = configs
        cacheLocation = configs.cacheLocation
    }

    fun initialize() {

        if (builder.cacheLocation == DEFAULT_PATH) logger.info { "Using Default path of [${DEFAULT_PATH.absolutePath}]" }

        when(builder.type) {
            TaskType.RUN_JS5 -> Js5Server.init(
                builder.cacheLocation.absolutePath,
                builder.js5Ports.toIntArray(), builder.revision,
                builder.supportPrefetch
            )
            TaskType.BUILD -> buildCache()
            TaskType.FRESH_INSTALL -> {
                DownloadOSRS.init()
                buildCache()
            }
        }
    }

    private fun buildCache() {
        val tempPath = FileUtil.getTemp()
        builder.cacheLocation.listFiles { file -> file.extension.contains("dat") || file.extension.contains("idx") }
            ?.forEach { file ->
                val tempFile = File(tempPath, file.name)
                file.copyTo(tempFile, true)
            }

        library = CacheLibrary((if(builder.type == TaskType.BUILD) FileUtil.getTemp() else FileUtil.getTempDir("cache")).toString())

        try {
            runPacking()
        } catch (e: Exception) {
            tempPath.delete()
            e.printStackTrace()
            logger.error { "Unable to build cache" }
            exitProcess(0)
        }
    }

    fun runPacking() {

        library.let {
            builder.extraTasks.forEach { task ->
                task.init(it)
            }

            it.update()
            it.rebuild(FileUtil.getTempDir("rebuilt"))
            it.close()

            val tempPath = builder.cacheLocation

            FileUtil.getTempDir("rebuilt").listFiles()?.filter { it.extension.contains("dat") || it.extension.contains("idx") }
                ?.forEach { file ->
                    val loc = File(tempPath,file.name)
                    file.copyTo(loc,true)
                }

            FileUtil.getTemp().deleteRecursively()
        }

    }

}