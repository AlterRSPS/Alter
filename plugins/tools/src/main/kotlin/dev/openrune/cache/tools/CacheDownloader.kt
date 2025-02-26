package dev.openrune.cache.tools

import com.google.gson.Gson
import dev.openrune.cache.util.FileUtil
import dev.openrune.cache.util.XteaLoader
import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Path
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.exception.ZipException
import me.tongfei.progressbar.ProgressBar
import org.yaml.snakeyaml.Yaml

private val logger = KotlinLogging.logger {}

/**
 * Tool to download the OSRS cache and XTEA keys for the specified revision
 * This is designed to be run as a Gradle task before the installation process
 */
object CacheDownloader {

    private const val CACHE_DOWNLOAD_LOCATION = "https://archive.openrs2.org/caches.json"
    private var dataFolder: File = Path.of("data").toFile()
    private var cacheFolder: File = Path.of("data", "cache").toFile()
    
    // Create a progress bar
    private fun progress(task: String, amt: Long): ProgressBar {
        // Use the simplest constructor
        return ProgressBar(task, amt)
    }
    
    @JvmStatic
    fun main(args: Array<String>) {
        val verbose = args.contains("--verbose")
        
        // Handle environment variables if provided
        val envDataDir = System.getenv("DOWNLOAD_DIR")
        val envCacheDir = System.getenv("CACHE_DIR")
        
        if (!envDataDir.isNullOrEmpty()) {
            val customDataFolder = File(envDataDir)
            if (customDataFolder.exists() || customDataFolder.mkdirs()) {
                println("Using custom data directory from environment: ${customDataFolder.absolutePath}")
                dataFolder = customDataFolder
            }
        }
        
        if (!envCacheDir.isNullOrEmpty()) {
            val customCacheFolder = File(envCacheDir)
            if (customCacheFolder.exists() || customCacheFolder.mkdirs()) {
                println("Using custom cache directory from environment: ${customCacheFolder.absolutePath}")
                cacheFolder = customCacheFolder
            }
        }
        
        // Read the revision from game.example.yml
        val revision = getRevisionFromConfig()
        println("Downloading OSRS cache and XTEA keys for revision $revision...")
        
        if (verbose) {
            println("Running in verbose mode")
            println("Data folder: ${dataFolder.absolutePath}")
            println("Cache folder: ${cacheFolder.absolutePath}")
        }
        
        // Ensure directories exist
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
            println("Created data folder at: ${dataFolder.absolutePath}")
        }
        if (!cacheFolder.exists()) {
            cacheFolder.mkdirs()
            println("Created cache folder at: ${cacheFolder.absolutePath}")
        }
        
        // Set the FileUtil cache location
        FileUtil.cacheLocation = cacheFolder
        println("Using temp directory: ${FileUtil.getTemp().absolutePath}")
        
        val time = measureTimeMillis {
            // Get the cache info for the specified revision
            println("Looking for cache with revision $revision...")
            val json = URL(CACHE_DOWNLOAD_LOCATION).readText()
            val caches: Array<CacheInfo> = Gson().fromJson(json, Array<CacheInfo>::class.java)
            
            // Use our custom findRevision function for the specific revision
            val cacheInfo = findRevisionSpecific(revision, caches)
            
            println("Found cache with ID: ${cacheInfo.id}, Revision: ${cacheInfo.builds.firstOrNull()?.major ?: "Unknown"}, Timestamp: ${cacheInfo.timestamp}")
            
            // Download the cache
            downloadCacheFile(cacheInfo)
            
            // Extract the cache
            extractCache()
            
            // Download and save XTEA keys
            saveCacheKeys(cacheInfo.id)
            
            // Clean up temp files
            println("Cleaning up temporary files...")
            cleanupTempFiles()
            
            // Verify the files were properly copied
            verifyFiles()
        }
        
        val minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60
        
        if (minutes > 0) {
            println("Cache downloaded and installed in $minutes minutes and $seconds seconds")
        } else {
            println("Cache downloaded and installed in $seconds seconds")
        }
        
        println("Cache and XTEA keys have been successfully downloaded and installed!")
    }

    private fun getRevisionFromConfig(): Int {
        val configFile = File("game.example.yml")
        if (!configFile.exists()) {
            logger.error { "Could not find game.example.yml configuration file" }
            return 228 // Default to revision 228 if the file is not found
        }
        
        try {
            val yaml = Yaml()
            val config = yaml.load<Map<String, Any>>(configFile.inputStream())
            val revision = config["revision"] as? Int ?: 228
            logger.info { "Using revision $revision from configuration" }
            return revision
        } catch (e: Exception) {
            logger.error(e) { "Error reading configuration file: ${e.message}" }
            return 228 // Default to revision 228 if there's an error reading the file
        }
    }

    /**
     * Find a cache with the specific revision number
     */
    private fun findRevisionSpecific(rev: Int, caches: Array<CacheInfo>): CacheInfo {
        return caches
            .filter { it.game.contains("oldschool") }
            .filter { it.builds.isNotEmpty() && it.builds[0].major == rev }
            .filter { it.timestamp != null }
            .maxByOrNull { it.timestamp } ?: error("Unable to find cache for revision: $rev")
    }
    
    /**
     * Download the cache file
     */
    private fun downloadCacheFile(cache: CacheInfo) {
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
                pb.stepTo(downloadedFileSize)
                out.write(data, 0, count)
            }
            pb.close()
            out.close()
            input.close()
            logger.info { "Cache download complete" }
        } catch (e: Exception) {
            logger.error { "Unable to download Cache: $e" }
            exitProcess(1)
        }
    }
    
    /**
     * Extract the cache from zip
     */
    private fun extractCache() {
        val path = File(FileUtil.getTemp(), "cache.zip")
        val zipFile = ZipFile(path)
        try {
            logger.info { "Extracting cache..." }
            zipFile.extractAll(FileUtil.getTemp().toString())
            
            // Find and copy cache files using a more flexible approach
            val tempDir = FileUtil.getTemp()
            logger.info { "Looking for cache files in ${tempDir.absolutePath}" }
            
            // Try multiple potential structures
            findAndCopyCacheFiles(tempDir)
            
            // Verify the files were copied
            val cacheFilesCount = cacheFolder.listFiles { file -> 
                file.name.endsWith(".dat2") || file.name.endsWith(".idx") 
            }?.size ?: 0
            
            if (cacheFilesCount > 0) {
                logger.info { "Successfully copied $cacheFilesCount cache files" }
            } else {
                logger.error { "No cache files were found or copied. Download may have failed." }
            }
            
        } catch (e: ZipException) {
            logger.error { "Unable to extract files from $path: $e" }
        }
    }
    
    /**
     * Find and copy cache files from extracted directory to cache directory
     */
    private fun findAndCopyCacheFiles(baseDir: File) {
        logger.info { "Searching for cache files in ${baseDir.absolutePath}" }
        
        // Strategy 1: Check for a "cache" directory
        val cacheDir = File(baseDir, "cache")
        if (cacheDir.exists() && cacheDir.isDirectory) {
            logger.info { "Found cache directory at ${cacheDir.absolutePath}" }
            cacheDir.listFiles()?.forEach { file ->
                val destFile = File(cacheFolder, file.name)
                file.copyTo(destFile, true)
                logger.info { "Copied ${file.name} to ${destFile.absolutePath}" }
            }
            return
        }
        
        // Strategy 2: Look for "main_file_cache" files directly in the temp dir
        val directCacheFiles = baseDir.listFiles { file -> 
            file.name.startsWith("main_file_cache") && (file.name.endsWith(".dat2") || file.name.endsWith(".idx")) 
        }
        
        if (directCacheFiles != null && directCacheFiles.isNotEmpty()) {
            logger.info { "Found ${directCacheFiles.size} cache files directly in ${baseDir.absolutePath}" }
            directCacheFiles.forEach { file ->
                val destFile = File(cacheFolder, file.name)
                file.copyTo(destFile, true)
                logger.info { "Copied ${file.name} to ${destFile.absolutePath}" }
            }
            return
        }
        
        // Strategy 3: Check for nested cache structure (e.g., disk/cache/)
        val potentialNestedDirs = listOf("disk", "oldschool", "runescape", "data")
        for (nestedDir in potentialNestedDirs) {
            val subDir = File(baseDir, nestedDir)
            if (subDir.exists() && subDir.isDirectory) {
                logger.info { "Found potential nested directory: ${subDir.absolutePath}" }
                
                // Check for cache dir within this subdir
                val nestedCacheDir = File(subDir, "cache")
                if (nestedCacheDir.exists() && nestedCacheDir.isDirectory) {
                    logger.info { "Found nested cache directory at ${nestedCacheDir.absolutePath}" }
                    nestedCacheDir.listFiles()?.forEach { file ->
                        val destFile = File(cacheFolder, file.name)
                        file.copyTo(destFile, true)
                        logger.info { "Copied ${file.name} to ${destFile.absolutePath}" }
                    }
                    return
                }
                
                // Also check for direct cache files in the nested dir
                val nestedCacheFiles = subDir.listFiles { file -> 
                    file.name.startsWith("main_file_cache") && (file.name.endsWith(".dat2") || file.name.endsWith(".idx")) 
                }
                
                if (nestedCacheFiles != null && nestedCacheFiles.isNotEmpty()) {
                    logger.info { "Found ${nestedCacheFiles.size} cache files in nested directory ${subDir.absolutePath}" }
                    nestedCacheFiles.forEach { file ->
                        val destFile = File(cacheFolder, file.name)
                        file.copyTo(destFile, true)
                        logger.info { "Copied ${file.name} to ${destFile.absolutePath}" }
                    }
                    return
                }
            }
        }
        
        // Strategy 4: Recursively search subdirectories for cache files (limited depth to avoid excessive searching)
        logger.info { "Searching subdirectories for cache files..." }
        val subdirs = baseDir.listFiles { file -> file.isDirectory && !potentialNestedDirs.contains(file.name) && file.name != "cache" }
        subdirs?.forEach { dir ->
            findAndCopyCacheFiles(dir)
        }
    }
    
    /**
     * Download and save XTEA keys
     */
    private fun saveCacheKeys(id: Int) {
        logger.info { "Downloading and saving XTEA keys..." }
        try {
            val url = "https://archive.openrs2.org/caches/${id}/keys.json"
            val xteaContent = URL(url).readText()
            
            // Save to data/xteas.json
            File(dataFolder, "xteas.json").writeText(xteaContent)
            
            // Also save to temp location for possible use by other tools
            File(FileUtil.getTemp(), "xteas.json").writeText(xteaContent)
            
            logger.info { "XTEA keys saved successfully" }
            
            // Verify XTEAs are in the expected format
            try {
                val xteas = Gson().fromJson(xteaContent, Array<Any>::class.java)
                if (xteas.isNotEmpty()) {
                    logger.info { "Downloaded ${xteas.size} XTEA keys" }
                }
            } catch (e: Exception) {
                logger.warn { "XTEA keys format may not be compatible: ${e.message}" }
            }
            
        } catch (e: Exception) {
            logger.error { "Unable to download and save XTEA keys: $e" }
        }
    }

    // Add new methods for cleanup and verification
    private fun cleanupTempFiles() {
        try {
            // Clean up individual files first
            val tempZip = File(FileUtil.getTemp(), "cache.zip")
            if (tempZip.exists()) {
                tempZip.delete()
                logger.info { "Deleted temporary zip file" }
            }
            
            // Clean up the temporary xteas.json file
            val tempXteas = File(FileUtil.getTemp(), "xteas.json")
            if (tempXteas.exists()) {
                tempXteas.delete()
                logger.info { "Deleted temporary XTEA keys file" }
            }
            
            // Clean up cache directory
            val tempCache = File(FileUtil.getTemp(), "cache")
            if (tempCache.exists() && tempCache.isDirectory) {
                tempCache.deleteRecursively()
                logger.info { "Deleted temporary cache directory" }
            }
            
            // Check for any remaining subdirectories from extraction
            val potentialDirs = listOf("disk", "oldschool", "runescape", "data")
            potentialDirs.forEach { dirName ->
                val dirPath = File(FileUtil.getTemp(), dirName)
                if (dirPath.exists() && dirPath.isDirectory) {
                    dirPath.deleteRecursively()
                    logger.info { "Deleted temporary directory: $dirName" }
                }
            }
            
            // Finally, try to delete the entire temp directory if it's now empty
            val tempDir = FileUtil.getTemp()
            val remainingFiles = tempDir.listFiles()?.size ?: 0
            
            if (remainingFiles == 0) {
                if (tempDir.delete()) {
                    logger.info { "Deleted empty temporary directory" }
                } else {
                    logger.info { "Could not delete temporary directory, but it's empty" }
                }
            } else {
                logger.info { "Temporary directory still contains $remainingFiles files/directories and was not deleted" }
            }
        } catch (e: Exception) {
            logger.warn { "Error during cleanup: ${e.message}" }
        }
    }

    private fun verifyFiles() {
        // Check if we have the main required files
        val requiredFiles = listOf(
            "main_file_cache.dat2",
            "main_file_cache.idx255"
        )
        
        val missingFiles = requiredFiles.filter { !File(cacheFolder, it).exists() }
        if (missingFiles.isEmpty()) {
            // Count total cache files
            val cacheFileCount = cacheFolder.listFiles { file -> 
                file.name.startsWith("main_file_cache") 
            }?.size ?: 0
            
            val xteaFile = File(dataFolder, "xteas.json")
            
            logger.info { "Verification complete: Found $cacheFileCount cache files" }
            logger.info { "XTEA file exists: ${xteaFile.exists()}" }
            
            if (xteaFile.exists() && cacheFileCount > 0) {
                logger.info { "All required files are in place" }
            } else {
                logger.error { "Some files are missing after download and extraction" }
            }
        } else {
            logger.error { "Missing critical files: ${missingFiles.joinToString(", ")}" }
        }
    }
} 