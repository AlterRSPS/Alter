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

            val cacheTool = CacheTool(
                Builder(type = TaskType.BUILD, revision = 228).apply {
                    extraTasks = arrayOf(
                        RemoveXteas(xteaLocation = xteaLocation.toFile())
                    )
                }
            )
            cacheTool.initialize()
            logger.info { "${xteaLocation.toAbsolutePath()} Renaming to ${backupXteaLocation.toAbsolutePath()}." }
            if (xteaLocation.toFile().renameTo(backupXteaLocation.toFile())) {
                println("${xteaLocation.toAbsolutePath()} was renamed to: ${backupXteaLocation.fileName}.")
                xteaLocation.createFile()
                xteaLocation.writeText("[]")
            } else {
                println("Failed to rename the file.")
                return
            }
        }
    }
}