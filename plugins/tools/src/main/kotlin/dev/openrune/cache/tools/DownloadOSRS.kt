package dev.openrune.cache.tools

import com.displee.cache.CacheLibrary
import com.google.gson.Gson
import dev.openrune.cache.util.XteaLoader
import dev.openrune.cache.tools.CacheTool.Constants.builder
import dev.openrune.cache.tools.CacheTool.Constants.library
import dev.openrune.cache.util.FileUtil
import dev.openrune.cache.util.progress
import dev.openrune.cache.util.stringToTimestamp
import dev.openrune.cache.util.toEchochUTC
import io.github.oshai.kotlinlogging.KotlinLogging
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.exception.ZipException
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

data class CacheInfo(
    val id: Int,
    val game: String,
    val timestamp: String,
    val builds: List<CacheInfoBuilds>,
    val sources: List<String>,
    val size: Long
)

data class CacheInfoBuilds(
    val major: Int
)

object DownloadOSRS {

    private val logger = KotlinLogging.logger {}

    private const val CACHE_DOWNLOAD_LOCATION = "https://archive.openrs2.org/caches.json"

    fun init() {

        val time = measureTimeMillis {
            val rev = builder.revision

            println("Looking for cache to download: ${if (rev == -1) "Latest" else rev}")
            val json = URL(CACHE_DOWNLOAD_LOCATION).readText()
            val caches: Array<CacheInfo> = Gson().fromJson(json, Array<CacheInfo>::class.java)
            val cacheInfo = if(rev == -1) getLatest(caches) else findRevision(rev,caches)

            downloadCache(cacheInfo)
            unZip()
            saveXteas(cacheInfo.id)
            XteaLoader.load()
            library = CacheLibrary(FileUtil.getTempDir("cache").toString())
            File(FileUtil.getTemp(), "/cache.zip").delete()
        }
        val hours = TimeUnit.MILLISECONDS.toHours(time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60
        if (hours > 0) {
            println("Cache Downloaded in ${hours} hours, $minutes minutes, and $seconds seconds")
        } else if (minutes > 0) {
            println("Cache Downloaded in $minutes minutes and $seconds seconds")
        } else {
            println("Cache Downloaded in $seconds seconds")
        }
    }

    private fun saveXteas(id: Int) {
        logger.info { "Saving Xteas" }
        try {
            val url = "https://archive.openrs2.org/caches/${id}/keys.json"
            File(FileUtil.getTemp(), "xteas.json").writeText(URL(url).readText())
            File(builder.cacheLocation, "xteas.json").writeText(URL(url).readText())
        } catch (e: Exception) {
            logger.error { "Unable to write Xteas $e" }
        }
    }

    private fun downloadCache(cache: CacheInfo) {
        try {
            val url = URL("https://archive.openrs2.org/caches/${cache.id}/disk.zip")
            val httpConnection = url.openConnection() as HttpURLConnection
            val completeFileSize = cache.size
            val input: InputStream = httpConnection.inputStream
            val out = FileOutputStream(File(FileUtil.getTemp(), "cache.zip"))

            val data = ByteArray(1024)
            var downloadedFileSize: Long = 0
            var count: Int

            val pb = progress("Downloading Cache", completeFileSize)

            while (input.read(data, 0, 1024).also { count = it } != -1) {
                downloadedFileSize += count.toLong()
                pb.stepBy(count.toLong())
                out.write(data, 0, count)
            }
            pb.close()
        } catch (e: Exception) {
            logger.error { "Unable to download Cache: $e" }
            exitProcess(0)
        }

    }

    private fun unZip() {
        val path = File(FileUtil.getTemp(), "cache.zip")
        val zipFile = ZipFile(path)
        try {
            logger.info { "Unzipping Cache please wait" }
            zipFile.extractAll(FileUtil.getTemp().toString())
        } catch (e: ZipException) {
            logger.error { "Unable extract files from $path : $e" }
        }
    }

    private fun getLatest(caches: Array<CacheInfo>) = caches
        .filter { it.game.contains("oldschool") }
        .filter { it.builds.isNotEmpty() }
        .filter { it.timestamp != null }
        .maxByOrNull { it.timestamp.stringToTimestamp().toEchochUTC()  } ?: error("Unable to find Latest Revision")


    private fun findRevision(rev: Int, caches: Array<CacheInfo>) = caches
        .filter { it.game.contains("oldschool") }
        .filter { it.builds.isNotEmpty() && it.builds[0].major == rev }
        .filter { it.timestamp != null }
        .maxByOrNull { it.timestamp.stringToTimestamp().toEchochUTC()   } ?: error("Unable to find Latest Revision: $rev")

}