package dev.openrune.cache.tools.tasks.impl.defs.json

import com.displee.cache.CacheLibrary
import com.google.gson.Gson
import dev.openrune.cache.CONFIGS
import dev.openrune.cache.ITEM
import dev.openrune.cache.filestore.buffer.BufferWriter
import dev.openrune.cache.filestore.definition.data.ItemType
import dev.openrune.cache.filestore.definition.encoder.ItemEncoder
import dev.openrune.cache.tools.tasks.CacheTask
import dev.openrune.cache.util.getFiles
import dev.openrune.cache.util.progress
import java.io.File

@Deprecated(
    message = "Deprecated since 1.2.4 due to conversion to TOML configuration: Use PackConfig with ConfigType.ITEMS instead",
    replaceWith = ReplaceWith("PackConfig(ConfigType.ITEMS)"),
    level = DeprecationLevel.WARNING // This will generate a warning during compilation, prompting the developer to migrate
)

class PackItems(private val itemDir : File) : CacheTask() {
    override fun init(library: CacheLibrary) {
        val size = getFiles(itemDir,"json").size
        val progress = progress("Packing Items", size)
        val errors : MutableMap<String, String> = emptyMap<String, String>().toMutableMap()
        if (size != 0) {
            getFiles(itemDir,"json").forEach {
                val def: ItemType = Gson().fromJson(it.readText(), ItemType::class.java)
                if (def.id == 0) {
                    errors[it.toString()] = "ID is 0 please set a id for the item to pack"
                    return@forEach
                }

                val encoder = ItemEncoder()
                val writer = BufferWriter(4096)
                with(encoder) { writer.encode(def) }
                library.index(CONFIGS).archive(ITEM)!!.add(def.id, writer.toArray())

                progress.step()
            }

            progress.close()

            errors.forEach {
                println("[ERROR] ${it.key} : ${it.value}")
            }

        }
    }

}