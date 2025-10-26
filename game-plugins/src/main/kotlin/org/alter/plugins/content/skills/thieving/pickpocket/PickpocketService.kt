package org.alter.plugins.content.skills.thieving.pickpocket

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
 * Loads the pickpocket configuration so that entries can be retrieved at runtime.
 */
class PickpocketService : Service {

    private val gson = Gson()

    val entries: ObjectArrayList<PickpocketEntry> = ObjectArrayList()

    private val entriesByNpc: Int2ObjectOpenHashMap<PickpocketEntry> = Int2ObjectOpenHashMap()

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        val file = Paths.get(serviceProperties.get("pickpockets") ?: "../data/cfg/thieving/pickpockets.json")

        Files.newBufferedReader(file).use { reader ->
            val listType = object : TypeToken<List<PickpocketEntry>>() {}.type
            val loaded: List<PickpocketEntry> = gson.fromJson(reader, listType)
            entries.addAll(loaded)
        }

        entries.forEach { entry ->
            entry.npcs.forEach { id ->
                entriesByNpc.put(getRSCM(id), entry)
            }
        }

        Server.logger.info { "Loaded ${entries.size.appendToString("pickpocket definition")}." }
    }

    fun lookup(npcId: Int): PickpocketEntry? = entriesByNpc[npcId]
}
