package org.alter.service

import dev.openrune.cache.CacheManager
import dev.openrune.cache.CacheManager.getItem
import dev.openrune.cache.CacheManager.getItems
import dev.openrune.cache.CacheManager.getNpcs
import dev.openrune.cache.CacheManager.getObjects
import dev.openrune.cache.CacheManager.itemSize
import dev.openrune.cache.CacheManager.npcSize
import gg.rsmod.util.Namer
import gg.rsmod.util.ServerProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import org.alter.game.Server
import org.alter.game.fs.DefinitionSet
import org.alter.game.model.World
import org.alter.game.service.Service
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
            outputPath = Paths.get(serviceProperties.getOrDefault("output-path", "../ids"))

            if (!Files.exists(outputPath)) {
                Files.createDirectory(outputPath)
                logger.info("Output path does not exist. Creating directory: $outputPath")
            } else if (!Files.isDirectory(outputPath)) {
                logger.error("Output path specified is a file - it must be a directory!")
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
        val definitions = world.definitions
        val namer = Namer()

        writeItems(definitions, namer)
        writeNpcs(definitions, namer)
        writeObjs(definitions, namer)
    }

    private fun writeItems(
        definitions: DefinitionSet,
        namer: Namer,
    ) {
        val count = itemSize()
        val items = generateWriter("Items.kt")
        for (i in 0 until count) {
            val item = getItems().get(i) ?: continue
            /*
             * Skip placeholder items.
             */
            if (item.isPlaceholder) {
                continue
            }
            val rawName = if (item.noteTemplateId > 0) getItem(item.noteLinkId).name + "_NOTED" else item.name
            if (rawName.isNotBlank()) {
                val name = namer.name(rawName, i)
                write(items, "const val $name = $i")
            }
        }
        endWriter(items)
    }

    private fun writeNpcs(
        definitions: DefinitionSet,
        namer: Namer,
    ) {
        val count = npcSize()
        val npcs = generateWriter("Npcs.kt")
        for (i in 0 until count) {
            val npc = getNpcs().get(i) ?: continue
            val rawName = npc.name.replace("?", "")
            if (rawName.isNotEmpty() && rawName.isNotBlank()) {
                val name = namer.name(npc.name, i)
                write(npcs, "const val $name = $i")
            }
        }
        endWriter(npcs)
    }

    private fun writeObjs(
        definitions: DefinitionSet,
        namer: Namer,
    ) {
        val count = CacheManager.objectSize()
        val objs = generateWriter("Objs.kt")
        for (i in 0 until count) {
            val npc = getObjects().get(i) ?: continue
            val rawName = npc.name.replace("?", "")
            if (rawName.isNotEmpty() && rawName.isNotBlank()) {
                val name = namer.name(npc.name, i)
                write(objs, "const val $name = $i")
            }
        }
        endWriter(objs)
    }

    private fun generateWriter(file: String): PrintWriter {
        val writer = PrintWriter(outputPath!!.resolve(file).toFile())
        writer.println("/* Auto-generated file using ${this::class.java} */")
        writer.println("package org.alter.api.cfg")
        writer.println("")
        writer.println("object ${file.removeSuffix(".kt")} {")
        writer.println("")
        return writer
    }

    private fun write(
        writer: PrintWriter,
        text: String,
    ) {
        writer.println("    $text")
    }

    private fun endWriter(writer: PrintWriter) {
        writer.println("    /* Auto-generated file using ${this::class.java} */")
        writer.println("}")
        writer.close()
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
