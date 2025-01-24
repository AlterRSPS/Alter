package org.alter.service

import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.CacheManager.getItems
import dev.openrune.cache.CacheManager.getNpc
import dev.openrune.cache.CacheManager.getNpcs
import dev.openrune.cache.CacheManager.getObject
import dev.openrune.cache.CacheManager.getObjects
import dev.openrune.cache.CacheManager.itemSize
import dev.openrune.cache.CacheManager.npcSize
import dev.openrune.cache.CacheManager.objectSize
import gg.rsmod.util.Namer
import gg.rsmod.util.ServerProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.service.Service
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DumpEntityIdService : Service {
    private var dump = false

    private var cachePath: Path? = null

    private var outputPath: Path? = null

    override fun init(
        server: Server,
        world: World,
        serviceProperties: ServerProperties,
    ) {
        dump = serviceProperties.getOrDefault("dump", false)
        if (dump) {
            cachePath = Paths.get(serviceProperties.get<String>("cache-path")!!)
            outputPath = Paths.get("../data/cfg/rscm/")

            if (!Files.exists(outputPath)) {
                Files.createDirectory(outputPath)
                logger.info { "Output path does not exist. Creating directory: $outputPath" }
            } else if (!Files.isDirectory(outputPath)) {
                logger.error { "Output path specified is a file - it must be a directory!" }
            }
        }
    }

    override fun postLoad(
        server: Server,
        world: World,
    ) {
        if (!dump) {
            return
        }
        val namer = Namer()
        writeItems(namer)
        writeNpcs(namer)
        writeObjects(namer)
    }


    private fun writeItems(
        namer: Namer,
    ) {
        val count = itemSize()
        val paths = outputPath!!.resolve("item"+".rscm")
        BufferedWriter((FileWriter(paths.toFile()))).use { itemWriter ->
            for (i in 0 until count) {
                val item = getItems()[i] ?: continue
                /*
                 * Skip placeholder items.
                 */
                if (item.isPlaceholder) {
                    continue
                }
                val rawName = if (item.noteTemplateId > 0) getItem(item.noteLinkId).name + "_NOTED" else item.name
                if (rawName.isNotBlank()) {
                    val name = namer.name(rawName, i)?.lowercase()
                    itemWriter.write("$name:$i\n")
                }
            }
        }
    }
    private fun writeNpcs(
        namer: Namer,
    ) {
        val count = npcSize()
        val paths = outputPath!!.resolve("npcs"+".rscm")
        BufferedWriter((FileWriter(paths.toFile()))).use { npcWriter ->
            for (i in 0 until count) {
                val npc = getNpcs()[i] ?: continue
                if (npc.name.isNotBlank()) {
                    val name = namer.name(npc.name, i)?.lowercase()
                    npcWriter.write("$name:$i\n")
                }
            }
        }
    }
    private fun writeObjects(
        namer: Namer,
    ) {
        val count = objectSize()
        val paths = outputPath!!.resolve("object"+".rscm")
        BufferedWriter((FileWriter(paths.toFile()))).use { objectWriter ->
            for (i in 0 until count) {
                val obj = getObjects()[i] ?: continue
                if (obj.name!!.isNotBlank()) {
                    val name = namer.name(obj.name, i)?.lowercase()
                    objectWriter.write("$name:$i\n")
                }
            }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
