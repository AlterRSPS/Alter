package dev.openrune.cache.tools.tasks.impl.defs.json

import com.displee.cache.CacheLibrary
import com.google.gson.Gson
import dev.openrune.cache.CONFIGS
import dev.openrune.cache.NPC
import dev.openrune.cache.filestore.buffer.BufferWriter
import dev.openrune.cache.filestore.definition.data.NpcType
import dev.openrune.cache.filestore.definition.encoder.NpcEncoder
import dev.openrune.cache.tools.tasks.CacheTask
import dev.openrune.cache.util.getFiles
import dev.openrune.cache.util.progress
import java.io.File

@Deprecated(
    message = "Deprecated since 1.2.4 due to conversion to TOML configuration: Use PackConfig with ConfigType.NPCS instead",
    replaceWith = ReplaceWith("PackConfig(ConfigType.NPCS)"),
    level = DeprecationLevel.WARNING // This will generate a warning during compilation, prompting the developer to migrate
)
class PackNpcs(private val npcDir : File) : CacheTask() {
    override fun init(library: CacheLibrary) {
        val size = getFiles(npcDir,"json").size
        val progress = progress("Packing Npcs", size)
        val errors : MutableMap<String, String> = emptyMap<String, String>().toMutableMap()
        if (size != 0) {
            getFiles(npcDir,"json").forEach {
                val def: NpcType = Gson().fromJson(it.readText(), NpcType::class.java)
                if (def.id == 0) {
                    errors[it.toString()] = "ID is 0 please set a id for the npc to pack"
                    return@forEach
                }

                val encoder = NpcEncoder()
                val writer = BufferWriter(4096)
                with(encoder) { writer.encode(def) }
                library.index(CONFIGS).archive(NPC)!!.add(def.id, writer.toArray())

                progress.step()
            }

            progress.close()

            errors.forEach {
                println("[ERROR] ${it.key} : ${it.value}")
            }

        }
    }

}