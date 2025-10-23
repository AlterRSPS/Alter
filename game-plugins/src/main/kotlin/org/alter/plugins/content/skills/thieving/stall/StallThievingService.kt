package org.alter.plugins.content.skills.thieving.stall

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gg.rsmod.util.ServerProperties
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.alter.api.ext.appendToString
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.service.Service
import org.alter.rscm.RSCM.getRSCM
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Loads the stall thieving configuration so that entries can be retrieved at runtime.
 */
class StallThievingService : Service {

    private val gson = Gson()

    val entries: ObjectArrayList<StallEntry> = ObjectArrayList()

    private val entriesByObject: Int2ObjectOpenHashMap<StallEntry> = Int2ObjectOpenHashMap()

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        val file = Paths.get(serviceProperties.get("stalls") ?: "../data/cfg/thieving/stalls.json")

        Files.newBufferedReader(file).use { reader ->
            val listType = object : TypeToken<List<StallEntry>>() {}.type
            val loaded: List<StallEntry> = gson.fromJson(reader, listType)
            entries.addAll(loaded)
        }

        entries.forEach { entry ->
            entry.objectIds = entry.objects.map { getRSCM(it) }.toIntArray()
            entry.emptyObjectId = getRSCM(entry.emptyObject)
            entry.objectIds.forEach { id ->
                entriesByObject.put(id, entry)
            }
        }

        Server.logger.info { "Loaded ${entries.size.appendToString("stall thieving definition")}." }
    }

    fun lookup(objectId: Int): StallEntry? = entriesByObject[objectId]
}
