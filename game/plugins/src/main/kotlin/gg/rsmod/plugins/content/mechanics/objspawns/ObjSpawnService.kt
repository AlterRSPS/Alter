package gg.rsmod.plugins.content.mechanics.objspawns

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import gg.rsmod.game.Server
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.StaticObject
import gg.rsmod.game.service.Service
import gg.rsmod.plugins.api.ext.npc
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.util.ServerProperties
import mu.KLogging
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author CloudS3c
 */
/*class ObjSpawnService : Service {

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        val path = Paths.get("./data/cfg/spawns/obj_spawns.yml")
        if (!Files.exists(path)) {
            throw FileNotFoundException("Path does not exist. $path")
        }
        val mapper = ObjectMapper(YAMLFactory())
        Files.newBufferedReader(path).use {
            val data = mapper.readValue(it, Array<ObjSpawn>::class.java)
            data.forEach { spawn ->
                world.queue {
                    val obj = DynamicObject(spawn.id, spawn.type, spawn.rotation, Tile(x = spawn.x, z = spawn.z))
                    world.spawn(obj)

                }
            }
            logger.info {"Loaded ${data.size} Objects spawned."}
        }
    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    companion object : KLogging()
}*/