package gg.rsmod.game.service.game

import gg.rsmod.game.Server
import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.*
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import mu.KLogging
import net.runelite.cache.util.Namer
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

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        dump = serviceProperties.getOrDefault("dump", false)
        if (dump) {
            cachePath = Paths.get(serviceProperties.get<String>("cache-path")!!)
            outputPath = Paths.get(serviceProperties.getOrDefault("output-path", "./ids"))

            if (!Files.exists(outputPath)) {
                Files.createDirectory(outputPath)
                logger.info("Output path does not exist. Creating directory: $outputPath")
            } else if (!Files.isDirectory(outputPath)) {
                logger.error("Output path specified is a file - it must be a directory!")
            }
        }
    }

    override fun postLoad(server: Server, world: World) {
        if (!dump) {
            return
        }
        val definitions = world.definitions
        val namer = Namer()

        writeItems(definitions, namer)
        writeNpcs(definitions, namer)
        writeObjs(definitions, namer)
        writeStructs(definitions, namer)
    }

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    private fun writeStructs(definitions: DefinitionSet, namer: Namer) {
        // Get all enums from cache
        val enumCount = definitions.getCount(EnumDef::class.java)
        val enums : MutableList<EnumDef> = mutableListOf()

        // Get all structs from cache
        val structCount = definitions.getCount(StructDef::class.java)
        val structs : MutableList<StructDef> = mutableListOf()

        for (s in 0 until structCount) {
            val struct = definitions.getNullable(StructDef::class.java, s) ?: continue
            structs.add(s, struct)
        }

        for (e in 0 until enumCount) {
            val enum = definitions.getNullable(EnumDef::class.java, e) ?: continue
            enums.add(e, enum)
        }

        val settingsFile = generateWriter("SettingCategories.kt")

        // Get all settings structs
        enums[422].values.forEach { (_, u) ->
            var name = ""

            structs[u as Int].params.forEach { (k, v) ->
                if(k == 744) {
                    name = namer.name(v.toString(), k)
                }

                if(k == 745 && name.isNotBlank()) {
                    write(settingsFile, "const val $name = $v")
                }
            }
        }

        endWriter(settingsFile)
    }

    private fun writeItems(definitions: DefinitionSet, namer: Namer) {
        val count = definitions.getCount(ItemDef::class.java)
        val items = generateWriter("Items.kt")
        for (i in 0 until count) {
            val item = definitions.getNullable(ItemDef::class.java, i) ?: continue
            /*
             * Skip placeholder items.
             */
            if (item.isPlaceholder) {
                continue
            }
            val rawName = if (item.noteTemplateId > 0) definitions.get(ItemDef::class.java, item.noteLinkId).name + "_NOTED" else item.name
            if (rawName.isNotBlank()) {
                val name = namer.name(rawName, i)
                write(items, "const val $name = $i")
            }
        }
        endWriter(items)
    }

    private fun writeNpcs(definitions: DefinitionSet, namer: Namer) {
        val count = definitions.getCount(NpcDef::class.java)
        val npcs = generateWriter("Npcs.kt")
        for (i in 0 until count) {
            val npc = definitions.getNullable(NpcDef::class.java, i) ?: continue
            val rawName = npc.name.replace("?", "")
            if (rawName.isNotEmpty() && rawName.isNotBlank()) {
                val name = namer.name(npc.name, i)
                write(npcs, "const val $name = $i")
            }
        }
        endWriter(npcs)
    }

    private fun writeObjs(definitions: DefinitionSet, namer: Namer) {
        val count = definitions.getCount(ObjectDef::class.java)
        val objs = generateWriter("Objs.kt")
        for (i in 0 until count) {
            val npc = definitions.getNullable(ObjectDef::class.java, i) ?: continue
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
        writer.println("package gg.rsmod.plugins.api.cfg")
        writer.println("")
        writer.println("object ${file.removeSuffix(".kt")} {")
        writer.println("")
        return writer
    }

    private fun write(writer: PrintWriter, text: String) {
        writer.println("    $text")
    }

    private fun endWriter(writer: PrintWriter) {
        writer.println("    /* Auto-generated file using ${this::class.java} */")
        writer.println("}")
        writer.close()
    }

    companion object : KLogging()
}
