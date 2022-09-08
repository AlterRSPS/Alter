package gg.rsmod.plugins.content.mechanics.npcdrops

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gg.rsmod.game.Server
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.plugins.api.ext.appendToString
import gg.rsmod.util.ServerProperties
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import mu.KLogging
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author Pepperxmint (113138126+pepperxmint@users.noreply.github.com)
 */
class NpcDropsService : Service {

    val drops = Int2ObjectOpenHashMap<ObjectArrayList<NpcDropSet>>()

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        val file = Paths.get(serviceProperties.get("npcdrops") ?: "./data/cfg/npc-drops.json")
        Files.newBufferedReader(file).use { reader ->
            val dropsJson = Gson().fromJson<Int2ObjectOpenHashMap<ObjectArrayList<NpcDropSet>>>(reader, object: TypeToken<Int2ObjectOpenHashMap<ObjectArrayList<NpcDropSet>>>() {}.type)
            val results = Int2ObjectOpenHashMap<ObjectArrayList<NpcDropSet>>()
            dropsJson.forEach { monsterDrops ->
                world.definitions.getNullable(NpcDef::class.java, monsterDrops.key) ?: return@forEach
                val validDrops = ObjectArrayList<NpcDropSet>()
                monsterDrops.value.forEach { dropset ->
                    world.definitions.getNullable(ItemDef::class.java, dropset.id) ?: return@forEach
                    validDrops.add(dropset)
                }
                results.put(monsterDrops.key, validDrops)
            }
            drops.putAll(results)
        }

        logger.info { "Loaded ${drops.size.appendToString("npc drops")}." }
    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    companion object : KLogging()
}