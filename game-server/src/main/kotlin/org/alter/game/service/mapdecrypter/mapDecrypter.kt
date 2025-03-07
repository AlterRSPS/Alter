package org.alter.game.service.mapdecrypter

import dev.openrune.cache.tools.Builder
import dev.openrune.cache.tools.CacheTool
import org.alter.game.service.Service
import dev.openrune.cache.tools.tasks.TaskType
import dev.openrune.cache.tools.tasks.impl.RemoveXteas
import org.alter.game.Server.Companion.logger
import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.writeText

class decryptMap : Service {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val xteaLocation = Path.of("./data/xteas.json")
            val backupXteaLocation = xteaLocation.resolveSibling("xteas.json.backup")

            // First, download the cache WITHOUT the RemoveXteas task
            val cacheTool = CacheTool(
                Builder(type = TaskType.FRESH_INSTALL, revision = 228)
                // No extraTasks here
            )
            cacheTool.initialize()
            
            // After downloading, THEN run the RemoveXteas as a separate step
            // Only if the file exists
            if (xteaLocation.toFile().exists()) {
                logger.info { "${xteaLocation.toAbsolutePath()} Renaming to ${backupXteaLocation.toAbsolutePath()}." }
                if (xteaLocation.toFile().renameTo(backupXteaLocation.toFile())) {
                    println("${xteaLocation.toAbsolutePath()} was renamed to: ${backupXteaLocation.fileName}.")
                    xteaLocation.createFile()
                    xteaLocation.writeText("[]")
                } else {
                    println("Failed to rename the file.")
                }
            } else {
                // Create an empty xteas file if it doesn't exist
                xteaLocation.createFile()
                xteaLocation.writeText("[]")
                println("Created empty xteas file as original was not found.")
            }
            
            // Optionally, you could run a separate task to apply the RemoveXteas
            // after the download if you still need that logic
            val decryptTool = CacheTool(
                Builder(type = TaskType.BUILD, revision = 228).apply {
                    extraTasks = arrayOf(
                        RemoveXteas(xteaLocation = xteaLocation.toFile())
                    )
                }
            )
            try {
                decryptTool.initialize()
            } catch (e: Exception) {
                println("Warning: Decryption step failed but cache was downloaded: ${e.message}")
                // Continue anyway since we have the cache
            }
        }
    }
}