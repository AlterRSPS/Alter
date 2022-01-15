package gg.rsmod.plugins.content.mechanics.itemspawns

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import gg.rsmod.game.Server
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import mu.KLogging
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author CloudS3c
 */
class ItemSpawnsService : Service {
    val spawns = ObjectArrayList<ItemSpawn>()

    override fun postLoad(server: Server, world: World) {
    }

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        val path = Paths.get("./data/cfg/spawns/item_spawns.yml")
        if (!Files.exists(path)) {
            throw FileNotFoundException("Path does not exist. $path")
        }
        val mapper = ObjectMapper(YAMLFactory())
        Files.newBufferedReader(path).use {
            val data = mapper.readValue(it, Array<ItemSpawn>::class.java)
            data.forEach { spawn ->
                val ground = GroundItem(spawn.id, spawn.amount, Tile(spawn.x, spawn.z, spawn.height))
                ground.respawnCycles = spawn.respawnTime
                world.spawn(ground)
            }
            logger.info {"Loaded ${data.size} Item Spawns."}
        }
    }

    companion object : KLogging()
}